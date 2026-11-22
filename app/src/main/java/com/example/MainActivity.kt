package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.auth.AdminAccessControl
import com.example.auth.signOutWithCleanup
import com.example.data.AiVerificationRepository
import com.example.data.GarageRepository
import com.example.data.PartSearchIndex
import com.example.export.PdfPartsExporter
import com.example.local.AppLocalDatabase
import com.example.local.LocalHistoryRepository
import com.example.model.UiState
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.GarageScreen
import com.example.ui.screens.HistoryReviewsScreen
import com.example.ui.screens.MaintenanceHubScreen
import com.example.ui.screens.PartsCatalogScreen
import com.example.ui.screens.VehicleSelectorScreen
import com.example.ui.theme.AutoPulseTheme
import com.example.ui.theme.TelemetryCyan
import com.example.viewmodel.AutoPulseViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

private object Routes {
  const val MAINTENANCE = "maintenance"
  const val CATALOG = "catalog"
  const val HISTORY = "history"
  const val GARAGE = "garage"
  const val ADMIN = "admin"
}

private data class NavTab(
  val route: String,
  val label: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val testTag: String,
)

private val StandardTabs = listOf(
  NavTab(Routes.MAINTENANCE, "Schedule", Icons.Rounded.AutoGraph, Icons.Outlined.AutoGraph, "tab_maintenance"),
  NavTab(Routes.CATALOG, "Parts", Icons.Rounded.Inventory2, Icons.Outlined.Inventory2, "tab_catalog"),
  NavTab(Routes.HISTORY, "History", Icons.Rounded.History, Icons.Outlined.History, "tab_history"),
  NavTab(Routes.GARAGE, "Garage", Icons.Rounded.DirectionsCar, Icons.Outlined.DirectionsCar, "tab_garage"),
)

private val AdminTab = NavTab(
  route = Routes.ADMIN,
  label = "Admin",
  selectedIcon = Icons.Rounded.AdminPanelSettings,
  unselectedIcon = Icons.Outlined.AdminPanelSettings,
  testTag = "tab_admin",
)

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Mandatory Phase 1 App Check wiring before any Compose UI or Firebase calls
    configureAppCheck(this, intent)
    enableEdgeToEdge()
    setContent {
      AutoPulseTheme {
        AppRootNavigation()
      }
    }
  }
}

@Composable
fun AppRootNavigation(auth: FirebaseAuth = Firebase.auth) {
  var currentUser by remember { mutableStateOf(auth.currentUser) }

  DisposableEffect(auth) {
    val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
      currentUser = firebaseAuth.currentUser
    }
    auth.addAuthStateListener(listener)
    onDispose { auth.removeAuthStateListener(listener) }
  }

  val user = currentUser
  Box(modifier = Modifier.fillMaxSize()) {
    if (user == null) {
      AuthScreen(
        onAuthSuccess = { currentUser = auth.currentUser },
      )
    } else {
      val uid = user.uid
      key(uid) {
        AuthenticatedAutoPulseApp(
          currentUserId = uid,
          userDisplayName = user.displayName ?: "Driver",
          userEmail = user.email,
          userPhotoUrl = user.photoUrl?.toString(),
        )
      }
    }

    // Global non-interactive EME Crest watermark across every screen
    Image(
      painter = painterResource(id = R.drawable.eme_crest_watermark),
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .align(Alignment.Center)
        .size(290.dp)
        .clip(CircleShape)
        .alpha(0.055f),
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticatedAutoPulseApp(
  currentUserId: String,
  userDisplayName: String,
  userEmail: String?,
  userPhotoUrl: String?,
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val credentialManager = remember(context) { CredentialManager.create(context) }

  val viewModel: AutoPulseViewModel = viewModel(
    key = currentUserId,
    factory = viewModelFactory {
      initializer {
        val app = checkNotNull(this[APPLICATION_KEY]) {
          "APPLICATION_KEY missing from CreationExtras"
        }
        val databaseId = app.getString(R.string.firestore_database_id)
        val db = FirebaseFirestore.getInstance(databaseId)
        val localDao = AppLocalDatabase.getInstance(app).localHistoryDao()
        AutoPulseViewModel(
          repository = GarageRepository(db),
          localHistoryRepository = LocalHistoryRepository(localDao),
          aiVerificationRepository = AiVerificationRepository(),
          currentUserId = currentUserId,
          userDisplayName = userDisplayName,
          userEmail = userEmail,
          userPhotoUrl = userPhotoUrl,
        )
      }
    },
  )

  val garageState by viewModel.garageState.collectAsStateWithLifecycle()
  val selectedSystem by viewModel.selectedSystem.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val inStockOnly by viewModel.inStockOnly.collectAsStateWithLifecycle()
  val highlightedPartId by viewModel.highlightedPartId.collectAsStateWithLifecycle()
  val selectedUrgency by viewModel.selectedUrgency.collectAsStateWithLifecycle()
  val isMutating by viewModel.isMutating.collectAsStateWithLifecycle()

  val aiPartState by viewModel.aiPartState.collectAsStateWithLifecycle()
  val aiScheduleState by viewModel.aiScheduleState.collectAsStateWithLifecycle()
  val localSearchHistory by viewModel.localSearchHistory.collectAsStateWithLifecycle()
  val localViewedParts by viewModel.localViewedParts.collectAsStateWithLifecycle()
  val localSelectedVehicles by viewModel.localSelectedVehicles.collectAsStateWithLifecycle()
  val pdfCartItems by viewModel.pdfCartItems.collectAsStateWithLifecycle()

  val adminUserLogsState by viewModel.adminUserLogsState.collectAsStateWithLifecycle()
  val loginAlertsState by viewModel.loginAlertsState.collectAsStateWithLifecycle()

  val snackbarHostState = remember { SnackbarHostState() }
  LaunchedEffect(viewModel) {
    viewModel.toastMessages.collect { message ->
      snackbarHostState.showSnackbar(message)
    }
  }

  when (val state = garageState) {
    is UiState.Loading -> {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          CircularProgressIndicator()
          Spacer(Modifier.height(12.dp))
          Text("Syncing Cloud Garage…", style = MaterialTheme.typography.bodyMedium)
        }
      }
    }

    is UiState.Error -> {
      Box(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center,
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text("Sync Error", style = MaterialTheme.typography.titleLarge)
          Spacer(Modifier.height(8.dp))
          Text(
            state.message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Spacer(Modifier.height(16.dp))
          Button(onClick = { signOutWithCleanup(credentialManager, scope) }) {
            Text("Sign Out & Retry")
          }
        }
      }
    }

    is UiState.Success -> {
      val snapshot = state.data
      val activeVehicle = snapshot.activeVehicle

      LaunchedEffect(snapshot.vehicles, activeVehicle?.id) {
        viewModel.onGarageSnapshotObserved(snapshot.vehicles, activeVehicle)
      }

      val catalogParts = remember(
        activeVehicle?.brand,
        activeVehicle?.model,
        activeVehicle?.year,
        activeVehicle?.variant,
        activeVehicle?.marketCode,
        activeVehicle?.powertrain,
      ) {
        viewModel.buildCatalogFor(activeVehicle)
      }
      val searchIndex = remember(catalogParts) { PartSearchIndex(catalogParts) }
      val filteredParts = remember(searchIndex, selectedSystem, searchQuery, inStockOnly) {
        viewModel.filterCatalog(searchIndex, selectedSystem, searchQuery, inStockOnly)
      }
      val schedule = remember(activeVehicle, snapshot.maintenanceLogs, catalogParts) {
        viewModel.computeScheduleFor(activeVehicle, snapshot.maintenanceLogs, catalogParts)
      }

      var forceShowSelector by remember { mutableStateOf(false) }
      val showOnboardingSelector = snapshot.vehicles.isEmpty() || forceShowSelector

      if (showOnboardingSelector) {
        VehicleSelectorScreen(
          canDismiss = snapshot.vehicles.isNotEmpty(),
          isSaving = isMutating,
          onSaveVehicle = { country, marketCode, brand, model, year, variant, powertrain, odoKm, nickname ->
            viewModel.saveNewVehicle(
              country = country,
              marketCode = marketCode,
              brand = brand,
              model = model,
              year = year,
              variant = variant,
              powertrain = powertrain,
              odometerKm = odoKm,
              nickname = nickname,
              onSuccess = { forceShowSelector = false },
            )
          },
          onBackDismiss = { forceShowSelector = false },
        )
      } else {
        val navController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route ?: Routes.MAINTENANCE

        // Strict RBAC Navigation Tabs: Admin tab is unlocked ONLY for mhossenali740@gmail.com
        val isSuperAdmin = viewModel.isSuperAdmin && AdminAccessControl.isSuperAdmin(userEmail)
        val visibleTabs = remember(isSuperAdmin) {
          if (isSuperAdmin) StandardTabs + AdminTab else StandardTabs
        }

        // Global Instant Search Bar State
        var showGlobalSearch by remember { mutableStateOf(false) }
        var globalSearchText by remember { mutableStateOf("") }
        val instantHits = remember(globalSearchText, searchIndex) {
          if (globalSearchText.trim().length < 2) emptyList() else searchIndex.search(globalSearchText).take(5)
        }

        val navigateToTab: (String) -> Unit = { targetRoute ->
          navController.navigate(targetRoute) {
            popUpTo(navController.graph.findStartDestination().id) {
              saveState = true
            }
            launchSingleTop = true
            restoreState = true
          }
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
          val useRail = maxWidth >= 600.dp

          Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
              Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                TopAppBar(
                  title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Image(
                        painter = painterResource(id = R.drawable.eme_crest_watermark),
                        contentDescription = "EME Crest",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                          .size(28.dp)
                          .clip(CircleShape)
                          .border(1.dp, TelemetryCyan.copy(alpha = 0.6f), CircleShape),
                      )
                      Spacer(Modifier.width(8.dp))
                      Text("AutoPulse", style = MaterialTheme.typography.titleLarge)
                      if (activeVehicle != null) {
                        Text(
                          text = "  •  ${activeVehicle.brand} ${activeVehicle.model}",
                          style = MaterialTheme.typography.bodyMedium,
                          color = TelemetryCyan,
                          maxLines = 1,
                          overflow = TextOverflow.Ellipsis,
                        )
                      }
                    }
                  },
                  actions = {
                    IconButton(
                      onClick = { showGlobalSearch = !showGlobalSearch },
                      modifier = Modifier.testTag("global_search_toggle_button"),
                    ) {
                      Icon(
                        imageVector = if (showGlobalSearch) Icons.Rounded.Close else Icons.Rounded.Search,
                        contentDescription = if (showGlobalSearch) "Close global search" else "Global OEM & parts search",
                      )
                    }
                  },
                  colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                  ),
                )

                AnimatedVisibility(visible = showGlobalSearch) {
                  Column(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(horizontal = 16.dp)
                      .padding(bottom = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                  ) {
                    OutlinedTextField(
                      value = globalSearchText,
                      onValueChange = { globalSearchText = it },
                      placeholder = { Text("Instant search by OEM # or Part Name…") },
                      leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                      singleLine = true,
                      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                      keyboardActions = KeyboardActions(
                        onSearch = {
                          if (globalSearchText.isNotBlank()) {
                            viewModel.commitSearchToHistory(globalSearchText, activeVehicle, instantHits.size)
                            viewModel.focusSearchInCatalog(globalSearchText)
                            showGlobalSearch = false
                            navigateToTab(Routes.CATALOG)
                          }
                        }
                      ),
                      shape = RoundedCornerShape(12.dp),
                      modifier = Modifier
                        .fillMaxWidth()
                        .testTag("global_instant_search_input"),
                    )
                    if (instantHits.isNotEmpty()) {
                      Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        tonalElevation = 4.dp,
                        modifier = Modifier.fillMaxWidth(),
                      ) {
                        Column {
                          instantHits.forEachIndexed { idx, hit ->
                            Row(
                              modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                  viewModel.commitSearchToHistory(globalSearchText, activeVehicle, instantHits.size)
                                  viewModel.focusCatalogPart(hit.system, hit.id)
                                  showGlobalSearch = false
                                  globalSearchText = ""
                                  navigateToTab(Routes.CATALOG)
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                                .testTag("global_hit_${hit.id}"),
                              verticalAlignment = Alignment.CenterVertically,
                              horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                              Column(modifier = Modifier.weight(1f)) {
                                Text(hit.name, style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(
                                  "${hit.oemNumber} • ${hit.system.displayName}",
                                  style = MaterialTheme.typography.labelSmall,
                                  color = TelemetryCyan,
                                )
                              }
                              Spacer(Modifier.width(8.dp))
                              Text(
                                hit.stockStatus.label,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary,
                              )
                            }
                            if (idx < instantHits.lastIndex) {
                              HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            },
            bottomBar = {
              if (!useRail) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                  visibleTabs.forEach { tab ->
                    val selected = currentRoute == tab.route
                    NavigationBarItem(
                      selected = selected,
                      onClick = { navigateToTab(tab.route) },
                      icon = {
                        Icon(
                          imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                          contentDescription = tab.label,
                        )
                      },
                      label = { Text(tab.label) },
                      modifier = Modifier.testTag(tab.testTag),
                    )
                  }
                }
              }
            },
          ) { innerPadding ->
            Row(
              modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            ) {
              if (useRail) {
                NavigationRail(
                  containerColor = MaterialTheme.colorScheme.surface,
                  modifier = Modifier.fillMaxHeight(),
                ) {
                  Spacer(Modifier.height(12.dp))
                  visibleTabs.forEach { tab ->
                    val selected = currentRoute == tab.route
                    NavigationRailItem(
                      selected = selected,
                      onClick = { navigateToTab(tab.route) },
                      icon = {
                        Icon(
                          imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                          contentDescription = tab.label,
                        )
                      },
                      label = { Text(tab.label) },
                      modifier = Modifier.testTag(tab.testTag),
                    )
                  }
                }
              }

              NavHost(
                navController = navController,
                startDestination = Routes.MAINTENANCE,
                modifier = Modifier.weight(1f).fillMaxHeight(),
              ) {
                composable(Routes.MAINTENANCE) {
                  MaintenanceHubScreen(
                    vehicle = activeVehicle,
                    schedule = schedule,
                    distanceUnit = snapshot.distanceUnit,
                    selectedUrgency = selectedUrgency,
                    aiScheduleState = aiScheduleState,
                    onSelectUrgency = viewModel::selectUrgencyFilter,
                    onSwitchVehicle = { forceShowSelector = true },
                    onUpdateOdometer = viewModel::updateOdometer,
                    onLogService = viewModel::logServiceCompleted,
                    onOpenPartInCatalog = { system, partId ->
                      viewModel.focusCatalogPart(system, partId)
                      navigateToTab(Routes.CATALOG)
                    },
                    onAuditScheduleWithAi = viewModel::auditScheduleWithAi,
                  )
                }

                composable(Routes.CATALOG) {
                  PartsCatalogScreen(
                    vehicle = activeVehicle,
                    distanceUnit = snapshot.distanceUnit,
                    allParts = catalogParts,
                    filteredParts = filteredParts,
                    savedParts = snapshot.savedParts,
                    pdfCartItems = pdfCartItems,
                    selectedSystem = selectedSystem,
                    searchQuery = searchQuery,
                    inStockOnly = inStockOnly,
                    highlightedPartId = highlightedPartId,
                    aiPartState = aiPartState,
                    onSelectSystem = viewModel::selectSystem,
                    onSearchQueryChange = viewModel::updateSearchQuery,
                    onCommitSearchHistory = { q, count ->
                      viewModel.commitSearchToHistory(q, activeVehicle, count)
                    },
                    onToggleInStockOnly = viewModel::toggleInStockOnly,
                    onClearHighlight = viewModel::clearHighlightedPart,
                    onSwitchVehicle = { forceShowSelector = true },
                    onToggleSavePart = { part, existingSavedId ->
                      viewModel.toggleSavedPart(part, activeVehicle, existingSavedId)
                    },
                    onPartInspected = { part ->
                      viewModel.recordPartViewed(part, activeVehicle)
                    },
                    onVerifyPartWithAi = { part ->
                      viewModel.verifyPartWithAi(part, activeVehicle)
                    },
                    onResetAiPartState = viewModel::resetAiPartState,
                    onAddAssemblyToPdf = { part ->
                      viewModel.addAssemblyToPdf(part, activeVehicle)
                    },
                    onAddSubPartToPdf = { parentPart, sub ->
                      viewModel.addSubComponentToPdf(parentPart, sub, activeVehicle)
                    },
                    onRemovePdfItem = viewModel::removePdfItem,
                    onClearPdfList = viewModel::clearPdfList,
                    onExportPdfShare = {
                      val exportRes = PdfPartsExporter.exportAndShare(context, activeVehicle, pdfCartItems)
                      exportRes.onFailure { err ->
                        scope.launch {
                          snackbarHostState.showSnackbar(err.localizedMessage ?: "PDF export failed")
                        }
                      }
                    },
                  )
                }

                composable(Routes.HISTORY) {
                  HistoryReviewsScreen(
                    viewedParts = localViewedParts,
                    searchHistory = localSearchHistory,
                    selectedVehicles = localSelectedVehicles,
                    distanceUnit = snapshot.distanceUnit,
                    onOpenPartInCatalog = { system, partId ->
                      viewModel.focusCatalogPart(system, partId)
                      navigateToTab(Routes.CATALOG)
                    },
                    onRunSearchInCatalog = { query ->
                      viewModel.focusSearchInCatalog(query)
                      navigateToTab(Routes.CATALOG)
                    },
                    onClearHistory = viewModel::clearLocalHistory,
                  )
                }

                composable(Routes.GARAGE) {
                  GarageScreen(
                    displayName = snapshot.profile?.displayName ?: userDisplayName,
                    email = snapshot.profile?.email ?: userEmail,
                    vehicles = snapshot.vehicles,
                    activeVehicle = activeVehicle,
                    maintenanceLogs = snapshot.maintenanceLogs,
                    savedParts = snapshot.savedParts,
                    distanceUnit = snapshot.distanceUnit,
                    onAddVehicle = { forceShowSelector = true },
                    onSelectActiveVehicle = viewModel::setActiveVehicle,
                    onUpdateOdometer = viewModel::updateOdometer,
                    onDeleteVehicle = viewModel::deleteVehicle,
                    onOpenSavedPart = { system, partId ->
                      viewModel.focusCatalogPart(system, partId)
                      navigateToTab(Routes.CATALOG)
                    },
                    onChangeDistanceUnit = viewModel::setDistanceUnit,
                    onSignOut = { signOutWithCleanup(credentialManager, scope) },
                  )
                }

                // Route-level RBAC Guard: even if navigated directly, redirect non-admin users immediately to Maintenance dashboard
                composable(Routes.ADMIN) {
                  if (!isSuperAdmin) {
                    LaunchedEffect(Unit) {
                      navController.navigate(Routes.MAINTENANCE) {
                        popUpTo(Routes.ADMIN) { inclusive = true }
                        launchSingleTop = true
                      }
                    }
                  } else {
                    AdminPanelScreen(
                      currentUserEmail = userEmail,
                      userLogsState = adminUserLogsState,
                      loginAlertsState = loginAlertsState,
                      onAcknowledgeAlert = viewModel::acknowledgeLoginAlert,
                      onUnauthorizedRedirect = {
                        navController.navigate(Routes.MAINTENANCE) {
                          popUpTo(Routes.ADMIN) { inclusive = true }
                          launchSingleTop = true
                        }
                      },
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
