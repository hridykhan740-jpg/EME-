const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_uid_101";
const BOB_UID = "bob_uid_202";
const ADMIN_UID = "super_admin_uid";
const ADMIN_EMAIL = "MhossenAli740@gmail.com";

const [emulatorHost, emulatorPortStr] = (
  process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085"
).split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user cannot read or write user profiles, vehicles, logs, or admin collections", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  const now = new Date(Date.now() - 1000);

  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
  await assertFails(unauthDb.collection("adminUserLogs").get());
  await assertFails(unauthDb.collection("loginAlerts").get());
  await assertFails(
    unauthDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("vehicles")
      .doc("veh_1")
      .set({
        userId: ALICE_UID,
        country: "Bangladesh",
        marketCode: "BD",
        brand: "Toyota",
        model: "Corolla Axio",
        year: 2021,
        variant: "1.5L Hybrid eCVT (NKE165)",
        powertrain: "HYBRID",
        odometerKm: 24000,
        createdAt: now,
        updatedAt: now,
      })
  );
});

test("Owner can manage vehicles, maintenanceLogs, savedParts; cross-user reads fail", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID, { email: "alice@gmail.com", email_verified: true }).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID, { email: "bob@gmail.com", email_verified: true }).firestore();
  const created = new Date(Date.now() - 10000);

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Alice Driver",
      email: "alice@gmail.com",
      distanceUnit: "KM",
      createdAt: created,
      updatedAt: created,
    })
  );

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("vehicles").doc("veh_1").set({
      userId: ALICE_UID,
      country: "Bangladesh",
      marketCode: "BD",
      brand: "Toyota",
      model: "Premio",
      year: 2020,
      variant: "1.5L F-EX Package (NZT260)",
      powertrain: "PETROL",
      odometerKm: 32000,
      createdAt: created,
      updatedAt: created,
    })
  );

  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("vehicles").get());
});

test("RBAC: Normal user & unverified email spoof CANNOT read adminUserLogs or loginAlerts; verified mhossenali740@gmail.com CAN", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID, {
    email: "alice@gmail.com",
    email_verified: true,
  }).firestore();
  const spoofDb = testEnv.authenticatedContext("spoof_uid", {
    email: "mhossenali740@gmail.com",
    email_verified: false,
  }).firestore();
  const adminDb = testEnv.authenticatedContext(ADMIN_UID, {
    email: ADMIN_EMAIL,
    email_verified: true,
  }).firestore();

  const created = new Date(Date.now() - 5000);

  // Alice writes her own login log and alert
  await assertSucceeds(
    aliceDb.collection("adminUserLogs").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Alice Driver",
      email: "alice@gmail.com",
      lastLoginEpochMs: Date.now() - 2000,
      loginCount: 1,
      selectedVehiclesSummary: "2020 Toyota Premio (BD)",
      createdAt: created,
      updatedAt: created,
    })
  );

  await assertSucceeds(
    aliceDb.collection("loginAlerts").doc("alert_alice_1").set({
      userId: ALICE_UID,
      displayName: "Alice Driver",
      email: "alice@gmail.com",
      targetAdminEmail: "mhossenali740@gmail.com",
      deliveryChannel: "FIRESTORE_ALERT_QUEUE",
      loginEpochMs: Date.now() - 2000,
      acknowledged: false,
      createdAt: created,
      updatedAt: created,
    })
  );

  // Normal user CANNOT list all user logs or read loginAlerts
  await assertFails(aliceDb.collection("adminUserLogs").get());
  await assertFails(aliceDb.collection("loginAlerts").get());

  // Unverified email spoof CANNOT read adminUserLogs or loginAlerts
  await assertFails(spoofDb.collection("adminUserLogs").get());
  await assertFails(spoofDb.collection("loginAlerts").get());

  // Verified Super-Admin mhossenali740@gmail.com (case-insensitive) CAN list both & acknowledge alerts
  await assertSucceeds(adminDb.collection("adminUserLogs").get());
  await assertSucceeds(adminDb.collection("loginAlerts").get());
  await assertSucceeds(
    adminDb.collection("loginAlerts").doc("alert_alice_1").update({
      acknowledged: true,
      updatedAt: new Date(Date.now() - 500),
    })
  );
});
