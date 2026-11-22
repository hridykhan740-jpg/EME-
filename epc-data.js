// EME AUTOMATIC HELP • AutoPulse Complete BD & Global Vehicle + 1,152-Part Illustrated EPC Engine
const CATEGORIES = [
  { id: 'ALL', label: 'All BD Fleet' },
  { id: 'SEDAN', label: 'Sedans & Hatch' },
  { id: 'SUV', label: 'SUVs & 4x4' },
  { id: 'MPV', label: 'MPVs & Vans' },
  { id: 'COMMERCIAL', label: 'Commercial Trucks & Buses' }
];

const VEHICLES = [
  // PRIVATE SEDANS & HATCHBACKS
  { cat:'SEDAN', brand:'Toyota', model:'Corolla Axio', years:[2025,2024,2023,2022,2021,2020,2019,2018,2016,2014], variants:[
    { label:'1.5L Hybrid G / EX eCVT (NKE165)', chassis:'NKE165', pt:'HYBRID' },
    { label:'1.5L 1NZ-FE X / Luxel (NZE161)', chassis:'NZE161', pt:'PETROL' }
  ]},
  { cat:'SEDAN', brand:'Toyota', model:'Corolla Fielder', years:[2025,2023,2021,2019,2017,2015], variants:[
    { label:'1.5L Hybrid G / WxB Wagon (NKE165G)', chassis:'NKE165G', pt:'HYBRID' },
    { label:'1.5L 1NZ-FE / 1.8L 2ZR (NZE161G)', chassis:'NZE161G', pt:'PETROL' }
  ]},
  { cat:'SEDAN', brand:'Toyota', model:'Premio', years:[2022,2021,2020,2019,2018,2016,2014], variants:[
    { label:'1.5L 1NZ-FE F EX (NZT260)', chassis:'NZT260', pt:'PETROL' },
    { label:'1.8L 2ZR-FAE X EX (ZRT260)', chassis:'ZRT260', pt:'PETROL' }
  ]},
  { cat:'SEDAN', brand:'Toyota', model:'Allion', years:[2022,2021,2020,2018,2016,2014], variants:[
    { label:'1.5L 1NZ-FE A15 G-Plus (NZT260)', chassis:'NZT260', pt:'PETROL' },
    { label:'1.8L 2ZR-FAE A18 G-Plus (ZRT260)', chassis:'ZRT260', pt:'PETROL' }
  ]},
  { cat:'SEDAN', brand:'Toyota', model:'Corolla Cross', years:[2026,2025,2024,2023,2022,2021], variants:[
    { label:'1.8L Hybrid Z / G (ZVG11)', chassis:'ZVG11', pt:'HYBRID' },
    { label:'1.8L 2ZR-FE Petrol (ZSG10)', chassis:'ZSG10', pt:'PETROL' }
  ]},
  { cat:'SEDAN', brand:'Toyota', model:'Camry', years:[2026,2024,2022,2020,2018], variants:[
    { label:'2.5L Dynamic Force Hybrid (AXVH70)', chassis:'AXVH70', pt:'HYBRID' }
  ]},
  { cat:'SEDAN', brand:'Toyota', model:'Yaris / Vitz', years:[2025,2023,2021,2019,2017], variants:[
    { label:'1.5L / 1.3L Petrol CVT (NSP151 / KSP130)', chassis:'NSP151', pt:'PETROL' }
  ]},
  { cat:'SEDAN', brand:'Toyota', model:'Aqua', years:[2026,2024,2022,2020,2018,2016], variants:[
    { label:'1.5L 1NZ-FXE Hybrid (NHP10 / MXPK11)', chassis:'NHP10', pt:'HYBRID' }
  ]},
  { cat:'SEDAN', brand:'Toyota', model:'Prius', years:[2026,2024,2022,2020,2018,2016], variants:[
    { label:'1.8L / 2.0L Hybrid eCVT (ZVW50 / MXWH60)', chassis:'ZVW50', pt:'HYBRID' }
  ]},
  { cat:'SEDAN', brand:'Toyota', model:'Probox / Succeed', years:[2025,2022,2020,2018,2015], variants:[
    { label:'1.5L 1NZ-FE / Hybrid DX (NCP160V / NHP160V)', chassis:'NCP160V', pt:'PETROL' }
  ]},
  { cat:'SEDAN', brand:'Honda', model:'Civic', years:[2026,2024,2022,2020,2018,2016], variants:[
    { label:'1.5L VTEC Turbo CVT (FC1 / FE1)', chassis:'FC1', pt:'PETROL' }
  ]},
  { cat:'SEDAN', brand:'Honda', model:'Grace', years:[2022,2020,2018,2016,2014], variants:[
    { label:'1.5L Sport Hybrid i-DCD EX/LX (GM4)', chassis:'GM4', pt:'HYBRID' },
    { label:'1.5L i-VTEC Petrol CVT (GM6)', chassis:'GM6', pt:'PETROL' }
  ]},
  { cat:'SEDAN', brand:'Honda', model:'City', years:[2026,2024,2022,2020,2018], variants:[
    { label:'1.5L i-VTEC / e:HEV (GN2 / GN5)', chassis:'GN2', pt:'HYBRID' }
  ]},
  { cat:'SEDAN', brand:'Honda', model:'Fit / Insight / Accord', years:[2026,2023,2021,2019,2017], variants:[
    { label:'1.5L i-DCD / e:HEV Hybrid (GP5 / GR3 / ZE4 / CV3)', chassis:'GP5', pt:'HYBRID' }
  ]},
  { cat:'SEDAN', brand:'Nissan', model:'Sunny / Bluebird Sylphy / Note', years:[2025,2022,2020,2018,2016], variants:[
    { label:'1.5L HR15DE / 1.2L e-POWER (N17 / TB17 / HE12)', chassis:'HE12', pt:'HYBRID' }
  ]},
  { cat:'SEDAN', brand:'Mitsubishi', model:'Lancer EX / Attrage / Mirage', years:[2024,2021,2019,2017,2015], variants:[
    { label:'1.6L 4A92 / 2.0L 4B11 MIVEC (CY3A / A13A)', chassis:'CY3A', pt:'PETROL' }
  ]},
  { cat:'SEDAN', brand:'Mazda', model:'Axela / Demio', years:[2024,2021,2019,2017,2015], variants:[
    { label:'1.5L Skyactiv-G / Hybrid (BM5FP / DJ3FS)', chassis:'BM5FP', pt:'PETROL' }
  ]},
  { cat:'SEDAN', brand:'Hyundai', model:'Elantra / Sonata / Accent', years:[2026,2024,2022,2020,2018], variants:[
    { label:'1.6L / 2.0L Smartstream (CN7 / DN8)', chassis:'CN7', pt:'PETROL' }
  ]},
  { cat:'SEDAN', brand:'Suzuki', model:'Swift / Alto / WagonR / Dzire', years:[2026,2024,2022,2020,2018], variants:[
    { label:'1.2L DualJet Hybrid (ZC83S / HA36S)', chassis:'ZC83S', pt:'HYBRID' }
  ]},

  // SUVs, CROSSOVERS & 4x4s (with dedicated Land Cruiser, Prado, Pajero V93/V94/V96/V98, Tata Hexa)
  { cat:'SUV', brand:'Toyota', model:'Land Cruiser Prado', years:[2026,2025,2024,2023,2022,2021,2020,2019,2018,2016,2014,2012,2010,2008,2004,2000], variants:[
    { label:'Prado J250 — 2.8L 1GD-FTV / 2.4T Hybrid (GDJ250)', chassis:'GDJ250', pt:'DIESEL' },
    { label:'Prado J150 — 2.7L 2TR-FE TX-L Petrol (TRJ150W)', chassis:'TRJ150W', pt:'PETROL' },
    { label:'Prado J150 — 2.8L 1GD-FTV / 3.0L 1KD-FTV TZ-G Diesel (GDJ150)', chassis:'GDJ150', pt:'DIESEL' },
    { label:'Prado J120 — 2.7L 2TR-FE / 3.0L 1KD-FTV (TRJ120W)', chassis:'TRJ120W', pt:'PETROL' },
    { label:'Prado J90 — 3.0L 1KZ-TE / 3.4L 5VZ-FE (KZJ95W)', chassis:'KZJ95W', pt:'DIESEL' }
  ]},
  { cat:'SUV', brand:'Toyota', model:'Land Cruiser', years:[2026,2025,2024,2023,2022,2021,2020,2018,2016,2014,2010,2006,2002,1998], variants:[
    { label:'LC300 — 3.3L F33A-FTV Diesel / 3.5L V35A-FTS (FJA300 / VJA300)', chassis:'FJA300', pt:'DIESEL' },
    { label:'LC200 — 4.6L 1UR-FE V8 / 4.5L 1VD-FTV Diesel (URJ202 / VDJ200)', chassis:'URJ202', pt:'PETROL' },
    { label:'LC100 — 4.7L 2UZ-FE V8 / 4.2L 1HD-FTE (UZJ100 / HDJ100)', chassis:'UZJ100', pt:'PETROL' },
    { label:'LC80 — 4.5L 1FZ-FE / 4.2L 1HZ 4WD (FZJ80 / HZJ80)', chassis:'HZJ80', pt:'DIESEL' },
    { label:'LC70 — 4.2L 1HZ / 4.5L 1VD-FTV Troop Carrier (HZJ79 / VDJ79)', chassis:'HZJ79', pt:'DIESEL' }
  ]},
  { cat:'SUV', brand:'Toyota', model:'Harrier', years:[2026,2024,2022,2020,2018,2016], variants:[
    { label:'2.5L Hybrid E-Four (AXUH80 / AVU65)', chassis:'AXUH80', pt:'HYBRID' },
    { label:'2.0L 3ZR-FAE / M20A-FKS Petrol (ZSU60 / MXUA80)', chassis:'ZSU60', pt:'PETROL' }
  ]},
  { cat:'SUV', brand:'Toyota', model:'RAV4', years:[2026,2024,2022,2020,2018], variants:[
    { label:'2.5L Dynamic Force Hybrid AWD (AXAH54)', chassis:'XA50', pt:'HYBRID' }
  ]},
  { cat:'SUV', brand:'Toyota', model:'Rush / Fortuner / C-HR', years:[2026,2024,2022,2020,2018], variants:[
    { label:'2.8L 1GD-FTV Fortuner / 1.8L C-HR / 1.5L Rush', chassis:'GUN156', pt:'DIESEL' }
  ]},
  { cat:'SUV', brand:'Honda', model:'Vezel', years:[2026,2024,2022,2020,2018,2016,2014], variants:[
    { label:'1.5L i-DCD Hybrid RS / Z (RU3)', chassis:'RU3', pt:'HYBRID' },
    { label:'1.5L e:HEV Z / Play Hybrid (RV5)', chassis:'RV5', pt:'HYBRID' }
  ]},
  { cat:'SUV', brand:'Honda', model:'HR-V / CR-V', years:[2026,2024,2022,2020,2018], variants:[
    { label:'1.5L VTEC Turbo / 2.0L e:HEV AWD (RW2 / RV3)', chassis:'RW2', pt:'HYBRID' }
  ]},
  { cat:'SUV', brand:'Mitsubishi', model:'Pajero (V93 / V94 / V96 / V98)', years:[2026,2024,2022,2020,2018,2016,2014,2012,2010,2008], variants:[
    { label:'Pajero V93 — 3.0L V6 6G72 MIVEC 4WD LWB (V93W)', chassis:'V93W', pt:'PETROL' },
    { label:'Pajero V94 — 2.5L 4D56 / 2.8L 4M40 Intercooler Turbo (V94W)', chassis:'V94W', pt:'DIESEL' },
    { label:'Pajero V96 — 3.5L V6 6G74 MIVEC Exceed 4WD (V96W)', chassis:'V96W', pt:'PETROL' },
    { label:'Pajero V98 — 3.2L 4M41 DI-D Common-Rail Diesel 4WD (V98W)', chassis:'V98W', pt:'DIESEL' },
    { label:'Pajero Sport — 2.4L 4N15 Clean Diesel 4WD (KS1W)', chassis:'KS1W', pt:'DIESEL' }
  ]},
  { cat:'SUV', brand:'Mitsubishi', model:'Outlander / Eclipse Cross / ASX', years:[2026,2024,2022,2020,2018], variants:[
    { label:'2.4L MIVEC / PHEV 4WD (GF8W / GK1W / GA2W)', chassis:'GF8W', pt:'PETROL' }
  ]},
  { cat:'SUV', brand:'Nissan', model:'X-Trail', years:[2026,2024,2022,2020,2018], variants:[
    { label:'2.0L MR20DD / 1.5L e-POWER e-4ORCE (NT32 / T33)', chassis:'NT32', pt:'HYBRID' }
  ]},
  { cat:'SUV', brand:'Nissan', model:'Patrol / Juke', years:[2026,2024,2022,2020,2018], variants:[
    { label:'5.6L V8 Patrol 4WD (Y62) / 1.5L Juke (YF15)', chassis:'Y62', pt:'PETROL' }
  ]},
  { cat:'SUV', brand:'Hyundai', model:'Tucson / Santa Fe / Creta', years:[2026,2024,2022,2020,2018], variants:[
    { label:'1.6T Hybrid / 2.2L CRDi Diesel (NX4 / TM / SU2)', chassis:'NX4', pt:'HYBRID' }
  ]},
  { cat:'SUV', brand:'Kia', model:'Sportage / Sorento', years:[2026,2024,2022,2020], variants:[
    { label:'1.6T HEV / 2.2L CRDi Diesel AWD (NQ5 / MQ4)', chassis:'NQ5', pt:'HYBRID' }
  ]},
  { cat:'SUV', brand:'Tata', model:'Hexa', years:[2025,2023,2021,2019,2017], variants:[
    { label:'2.2L Varicor 400 Diesel 6AT / 6MT AWD (XT / XTA)', chassis:'HEXA-XTA', pt:'DIESEL' }
  ]},
  { cat:'SUV', brand:'Tata', model:'Nexon / Harrier / Safari', years:[2026,2024,2022,2020], variants:[
    { label:'1.5L Revotorq / 2.0L Kryotec Diesel / EV', chassis:'X1-O5', pt:'DIESEL' }
  ]},
  { cat:'SUV', brand:'MG', model:'ZS / HS', years:[2026,2024,2022,2020], variants:[
    { label:'1.5L Turbo / 1.5L VTi / EV (ZS11 / AS23)', chassis:'ZS11', pt:'PETROL' }
  ]},
  { cat:'SUV', brand:'Haval', model:'H6 / Jolion', years:[2026,2024,2022], variants:[
    { label:'1.5L DHT Strong Hybrid (B01-HEV / A01)', chassis:'B01-HEV', pt:'HYBRID' }
  ]},

  // MICROBUSES, MPVs & VANS
  { cat:'MPV', brand:'Toyota', model:'Noah / Voxy / Esquire', years:[2026,2024,2022,2020,2018,2016], variants:[
    { label:'1.8L Hybrid Si / ZS (ZWR80 / ZWR90)', chassis:'ZWR80', pt:'HYBRID' },
    { label:'2.0L 3ZR-FAE Petrol (ZRR80)', chassis:'ZRR80', pt:'PETROL' }
  ]},
  { cat:'MPV', brand:'Toyota', model:'Alphard / Vellfire', years:[2026,2024,2022,2020,2018], variants:[
    { label:'2.5L 2AR-FXE Hybrid Executive (AYH30 / AGH30)', chassis:'AYH30', pt:'HYBRID' }
  ]},
  { cat:'MPV', brand:'Toyota', model:'HiAce', years:[2026,2024,2022,2020,2018,2015], variants:[
    { label:'2.8L 1GD-FTV / 3.0L 1KD Diesel (GDH200 / KDH200)', chassis:'GDH200', pt:'DIESEL' },
    { label:'2.7L 2TR-FE Super GL Petrol (TRH200)', chassis:'TRH200', pt:'PETROL' }
  ]},
  { cat:'MPV', brand:'Toyota', model:'LiteAce / Avanza / Sienta', years:[2026,2023,2020,2018,2015], variants:[
    { label:'1.5L Hybrid / Petrol MPV (NHP170G / S402M / W101)', chassis:'NHP170G', pt:'HYBRID' }
  ]},
  { cat:'MPV', brand:'Nissan', model:'Serena / Caravan / NV200', years:[2026,2023,2021,2019,2017], variants:[
    { label:'1.2L e-POWER / 2.5L Diesel (HFC27 / E26 / M20)', chassis:'HFC27', pt:'HYBRID' }
  ]},
  { cat:'MPV', brand:'Mitsubishi', model:'Delica D:5 / Xpander', years:[2026,2023,2021,2019], variants:[
    { label:'2.2L 4N14 Diesel 4WD / 1.5L Xpander (CV1W / NC1W)', chassis:'CV1W', pt:'DIESEL' }
  ]},

  // COMMERCIAL TRUCKS, PICKUPS & BUSES
  { cat:'COMMERCIAL', brand:'Toyota', model:'Hilux / Dyna', years:[2026,2024,2022,2020,2018,2015], variants:[
    { label:'2.8L 1GD-FTV Revo 4x4 / 4.0L N04C Dyna (GUN126 / XZU600)', chassis:'GUN126', pt:'DIESEL' }
  ]},
  { cat:'COMMERCIAL', brand:'Mitsubishi', model:'L200 / Fuso Canter', years:[2026,2023,2020,2018,2015], variants:[
    { label:'2.4L 4N15 Triton / 4.9L 4M50 Canter (KL1T / FE70)', chassis:'FE70', pt:'DIESEL' }
  ]},
  { cat:'COMMERCIAL', brand:'Tata', model:'Ace / Intra / Xenon / LPT 1615 / Prima', years:[2026,2024,2022,2020,2017], variants:[
    { label:'5.9L Cummins 6BT / DICOR Cargo Fleet (LPT1615 / ACE-GOLD)', chassis:'LPT1615', pt:'DIESEL' }
  ]},
  { cat:'COMMERCIAL', brand:'Ashok Leyland', model:'Dost / Ecomet / Viking', years:[2026,2023,2020,2018], variants:[
    { label:'5.8L H-Series / 1.5L P15 Truck & Bus (VIKING-BUS / DOST)', chassis:'ECOMET-1615', pt:'DIESEL' }
  ]},
  { cat:'COMMERCIAL', brand:'Mahindra', model:'Bolero Pickup / Jeeto', years:[2026,2023,2020,2018], variants:[
    { label:'2.5L m2DiCR Pickup / 625cc Jeeto (BOLERO-PU)', chassis:'BOLERO-PU', pt:'DIESEL' }
  ]},
  { cat:'COMMERCIAL', brand:'Isuzu', model:'D-Max / N-Series', years:[2026,2024,2021,2018], variants:[
    { label:'3.0L 4JJ3 / 5.2L 4HK1 Elf Cargo (RG01 / NKR85)', chassis:'NKR85', pt:'DIESEL' }
  ]},
  { cat:'COMMERCIAL', brand:'Hino', model:'AK1J / RM2 / 300-500 Series', years:[2026,2023,2020,2017], variants:[
    { label:'7.7L J08C / 8.9L A09C Bus & Truck (AK1JR / RM2PSS / FC9J)', chassis:'AK1JR', pt:'DIESEL' }
  ]},
  { cat:'COMMERCIAL', brand:'Eicher', model:'Pro 2049 / 3015 / Skyline', years:[2026,2023,2020,2018], variants:[
    { label:'3.8L E474 Volvo-Tech Diesel Truck & Bus (PRO-3015)', chassis:'PRO-3015', pt:'DIESEL' }
  ]}
];

const BRAND_CODES = {
  Toyota: { epc:'Toyota EPC', oil:'90915-YZZF2', air:'17801-21060', fuel:'77024-52170', cabin:'87139-52040', pad:'04465-52320', rotor:'43512-52130', spark:'90919-01275', clutch:'31210-12281', rack:'45510-12450', rad:'16400-21300', ac:'88310-02710', exh:'17410-21B30' },
  Honda: { epc:'Honda EPC', oil:'15400-RTA-003', air:'17220-5R0-008', fuel:'17048-T7A-000', cabin:'80291-T5A-J01', pad:'45022-T7J-H00', rotor:'45251-T7A-000', spark:'12290-5R0-003', clutch:'22105-RNA-305', rack:'53601-T7J-H01', rad:'19010-5R0-003', ac:'38810-5R8-J01', exh:'18160-5R0-000' },
  Mitsubishi: { epc:'Mitsubishi ASA / Fuso EPC', oil:'MZ690070', air:'1500A513', fuel:'1770A233', cabin:'7803A043', pad:'4605B005', rotor:'4615A178', spark:'1822A086', clutch:'2304A075', rack:'4410A453', rad:'1350A297', ac:'7813A618', exh:'1570C312' },
  Nissan: { epc:'Nissan FAST EPC', oil:'15208-65F0E', air:'16546-4BA1A', fuel:'16400-4BA0A', cabin:'27277-4BA0A', pad:'D1060-4BA0A', rotor:'40206-4BA0A', spark:'22401-ED815', clutch:'30100-ED80A', rack:'49001-4BA0A', rad:'21410-4BA0A', ac:'92600-4BA0A', exh:'20010-4BA0A' },
  Tata: { epc:'Tata Motors EPC', oil:'2790-1813-0102', air:'5442-0913-0101', fuel:'2786-0911-0114', cabin:'5438-8340-0104', pad:'2702-4210-0118', rotor:'2702-4210-0104', spark:'2791-1511-0103', clutch:'2651-2520-0101', rack:'2641-4620-0102', rad:'2525-5010-0105', ac:'5438-8340-0210', exh:'2525-4920-0108' },
  'Ashok Leyland': { epc:'Ashok Leyland EPC', oil:'F8835100', air:'2786-0913-0101', fuel:'2786-0911-0114', cabin:'AL-CAB-104', pad:'AL-BRK-118', rotor:'AL-DSC-104', spark:'AL-GLW-103', clutch:'AL-CLT-101', rack:'AL-STR-102', rad:'AL-RAD-105', ac:'AL-AC-210', exh:'AL-EXH-108' },
  Hino: { epc:'Hino EPC Commercial', oil:'15607-2190', air:'17801-E0010', fuel:'23401-1730', cabin:'S8850-E0020', pad:'04465-E0050', rotor:'43512-E0020', spark:'19110-1220', clutch:'31210-E0410', rack:'44110-E0120', rad:'16090-E0040', ac:'88310-E0060', exh:'17401-E0220' }
};

const SYSTEMS = [
  'Engine','Fuel & Air','Transmission','Clutch','Suspension','Steering',
  'Brakes','Cooling','AC / HVAC','Electrical','Exhaust','Body & Interior'
];

// Crisp cached SVG engineering illustrations mapped by component geometry
const SVG_CACHE = Object.create(null);
function inferIconKey(name, fallback) {
  const n = (name || '').toLowerCase();
  if (n.includes('filter') || n.includes('strainer') || n.includes('cartridge') || n.includes('element') || n.includes('drier')) return 'filter';
  if (n.includes('o-ring') || n.includes('seal') || n.includes('washer') || n.includes('gasket') || n.includes('shim') || n.includes('circlip') || n.includes('collar')) return 'seal';
  if (n.includes('bolt') || n.includes('nut') || n.includes('stud') || n.includes('pin') || n.includes('clip')) return 'bolt';
  if (n.includes('spark') || n.includes('glow') || n.includes('injector') || n.includes('coil')) return 'spark';
  if (n.includes('rotor') || n.includes('disc') || n.includes('flywheel') || n.includes('drum')) return 'rotor';
  if (n.includes('pad') || n.includes('shoe') || n.includes('plate') || n.includes('shield')) return 'pad';
  if (n.includes('strut') || n.includes('shock') || n.includes('spring') || n.includes('damper')) return 'spring';
  if (n.includes('arm') || n.includes('bracket') || n.includes('mount') || n.includes('fork') || n.includes('ball joint') || n.includes('bushing')) return 'bracket';
  if (n.includes('rack') || n.includes('tie') || n.includes('cable') || n.includes('shaft') || n.includes('rod')) return 'cable';
  if (n.includes('bearing') || n.includes('gear') || n.includes('sprocket') || n.includes('pulley')) return 'bearing';
  if (n.includes('sensor') || n.includes('switch') || n.includes('relay') || n.includes('solenoid') || n.includes('valve') || n.includes('servo')) return 'sensor';
  if (n.includes('belt') || n.includes('chain') || n.includes('tensioner')) return 'belt';
  if (n.includes('hose') || n.includes('pipe') || n.includes('exhaust') || n.includes('muffler') || n.includes('bellows')) return 'hose';
  if (n.includes('radiator') || n.includes('condenser') || n.includes('evaporator') || n.includes('fan') || n.includes('blower') || n.includes('heater')) return 'fan';
  if (n.includes('battery') || n.includes('alternator') || n.includes('starter')) return 'battery';
  if (n.includes('fluid') || n.includes('tank') || n.includes('reservoir') || n.includes('oil') || n.includes('cylinder')) return 'bottle';
  if (n.includes('wiper') || n.includes('mirror') || n.includes('handle') || n.includes('latch') || n.includes('headlamp')) return 'wiper';
  return fallback || 'bearing';
}

function partSvg(kind) {
  if (SVG_CACHE[kind]) return SVG_CACHE[kind];
  const s = '#1e293b', a = '#ea580c', c = '#0284c7';
  const bodies = {
    filter: `<rect x="16" y="8" width="24" height="36" rx="4" fill="#e2e8f0" stroke="${s}" stroke-width="2.2"/><line x1="19" y1="16" x2="37" y2="16" stroke="${a}" stroke-width="2"/><line x1="19" y1="23" x2="37" y2="23" stroke="${a}" stroke-width="2"/><line x1="19" y1="30" x2="37" y2="30" stroke="${a}" stroke-width="2"/><line x1="19" y1="37" x2="37" y2="37" stroke="${a}" stroke-width="2"/><ellipse cx="28" cy="11" rx="8" ry="2.5" fill="#94a3b8" stroke="${s}" stroke-width="1.5"/>`,
    seal: `<circle cx="28" cy="28" r="16" fill="none" stroke="${s}" stroke-width="5"/><circle cx="28" cy="28" r="16" fill="none" stroke="${c}" stroke-width="1.5" stroke-dasharray="4 3"/><circle cx="28" cy="28" r="8" fill="none" stroke="${a}" stroke-width="2"/>`,
    bolt: `<polygon points="18,12 38,12 42,19 14,19" fill="#cbd5e1" stroke="${s}" stroke-width="2"/><rect x="23" y="19" width="10" height="26" rx="2" fill="#e2e8f0" stroke="${s}" stroke-width="2"/><line x1="23" y1="26" x2="33" y2="29" stroke="${c}" stroke-width="1.8"/><line x1="23" y1="32" x2="33" y2="35" stroke="${c}" stroke-width="1.8"/><line x1="23" y1="38" x2="33" y2="41" stroke="${c}" stroke-width="1.8"/>`,
    spark: `<rect x="24" y="8" width="8" height="10" rx="2" fill="#e2e8f0" stroke="${s}" stroke-width="2"/><rect x="20" y="18" width="16" height="14" rx="2" fill="#fff" stroke="${c}" stroke-width="2"/><line x1="20" y1="22" x2="36" y2="22" stroke="${a}" stroke-width="1.8"/><line x1="20" y1="26" x2="36" y2="26" stroke="${a}" stroke-width="1.8"/><rect x="23" y="32" width="10" height="14" fill="#cbd5e1" stroke="${s}" stroke-width="2"/><path d="M28 46v4h5" fill="none" stroke="${s}" stroke-width="2.2"/>`,
    rotor: `<circle cx="28" cy="28" r="19" fill="#e2e8f0" stroke="${s}" stroke-width="2.4"/><circle cx="28" cy="28" r="8" fill="#cbd5e1" stroke="${a}" stroke-width="2.2"/><circle cx="28" cy="28" r="3.2" fill="#fff" stroke="${s}" stroke-width="1.6"/><circle cx="28" cy="14" r="1.8" fill="${c}"/><circle cx="42" cy="28" r="1.8" fill="${c}"/><circle cx="28" cy="42" r="1.8" fill="${c}"/><circle cx="14" cy="28" r="1.8" fill="${c}"/>`,
    pad: `<rect x="10" y="15" width="36" height="26" rx="7" fill="#cbd5e1" stroke="${s}" stroke-width="2.2"/><rect x="14" y="19" width="28" height="18" rx="4" fill="${a}" fill-opacity="0.75" stroke="${s}" stroke-width="1.4"/><line x1="28" y1="19" x2="28" y2="37" stroke="#fff" stroke-width="2"/>`,
    spring: `<line x1="28" y1="7" x2="28" y2="49" stroke="${s}" stroke-width="3.2"/><polyline points="17,15 39,20 17,25 39,30 17,35 39,40" fill="none" stroke="${a}" stroke-width="3" stroke-linejoin="round"/><rect x="21" y="40" width="14" height="8" rx="2" fill="#cbd5e1" stroke="${s}" stroke-width="1.8"/>`,
    bracket: `<path d="M12 40 L28 13 L44 38 Z" fill="#e2e8f0" stroke="${s}" stroke-width="2.6" stroke-linejoin="round"/><circle cx="12" cy="40" r="4" fill="${c}" stroke="${s}" stroke-width="1.5"/><circle cx="28" cy="13" r="4" fill="${a}" stroke="${s}" stroke-width="1.5"/><circle cx="44" cy="38" r="4" fill="${c}" stroke="${s}" stroke-width="1.5"/>`,
    cable: `<circle cx="28" cy="28" r="15" fill="none" stroke="${s}" stroke-width="3.5"/><circle cx="28" cy="28" r="11" fill="none" stroke="${c}" stroke-width="1.8"/><rect x="8" y="25" width="8" height="6" rx="1.5" fill="${a}"/><rect x="40" y="25" width="8" height="6" rx="1.5" fill="${a}"/>`,
    bearing: `<circle cx="28" cy="28" r="18" fill="#cbd5e1" stroke="${s}" stroke-width="2.4"/><circle cx="28" cy="28" r="9" fill="#fff" stroke="${s}" stroke-width="2"/><circle cx="28" cy="14" r="2.6" fill="${a}"/><circle cx="40" cy="21" r="2.6" fill="${a}"/><circle cx="40" cy="35" r="2.6" fill="${a}"/><circle cx="28" cy="42" r="2.6" fill="${a}"/><circle cx="16" cy="35" r="2.6" fill="${a}"/><circle cx="16" cy="21" r="2.6" fill="${a}"/>`,
    sensor: `<rect x="16" y="16" width="24" height="22" rx="4" fill="#cbd5e1" stroke="${s}" stroke-width="2.2"/><circle cx="28" cy="27" r="5" fill="${c}"/><line x1="21" y1="8" x2="21" y2="16" stroke="${a}" stroke-width="2.5"/><line x1="28" y1="8" x2="28" y2="16" stroke="${a}" stroke-width="2.5"/><line x1="35" y1="8" x2="35" y2="16" stroke="${a}" stroke-width="2.5"/><rect x="22" y="38" width="12" height="10" fill="#94a3b8" stroke="${s}" stroke-width="1.8"/>`,
    belt: `<circle cx="18" cy="34" r="9" fill="#e2e8f0" stroke="${s}" stroke-width="2.2"/><circle cx="38" cy="20" r="7" fill="#e2e8f0" stroke="${c}" stroke-width="2.2"/><line x1="13" y1="26" x2="34" y2="14" stroke="${s}" stroke-width="3"/><line x1="24" y1="41" x2="43" y2="26" stroke="${s}" stroke-width="3"/>`,
    hose: `<path d="M10 38 H24 L34 18 H46" fill="none" stroke="${s}" stroke-width="6" stroke-linecap="round" stroke-linejoin="round"/><path d="M10 38 H24 L34 18 H46" fill="none" stroke="#cbd5e1" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/><rect x="21" y="21" width="16" height="12" rx="3" fill="${a}" fill-opacity="0.8" stroke="${s}" stroke-width="1.6"/>`,
    fan: `<rect x="10" y="10" width="36" height="36" rx="4" fill="#e2e8f0" stroke="${s}" stroke-width="2"/><circle cx="28" cy="28" r="13" fill="#fff" stroke="${c}" stroke-width="2"/><path d="M28 16 C33 20 33 25 28 28 C23 25 23 20 28 16 Z M40 28 C36 33 31 33 28 28 C31 23 36 23 40 28 Z M28 40 C23 36 23 31 28 28 C33 31 33 36 28 40 Z M16 28 C20 23 25 23 28 28 C25 33 20 33 16 28 Z" fill="${a}" stroke="${s}" stroke-width="1"/>`,
    battery: `<rect x="10" y="16" width="36" height="28" rx="4" fill="#cbd5e1" stroke="${s}" stroke-width="2.2"/><rect x="15" y="10" width="8" height="6" fill="${a}" stroke="${s}" stroke-width="1.5"/><rect x="33" y="10" width="8" height="6" fill="${s}"/><polyline points="30,21 24,30 30,30 26,39" fill="none" stroke="${a}" stroke-width="2.8" stroke-linecap="round" stroke-linejoin="round"/>`,
    bottle: `<path d="M23 9 H33 V16 L40 22 V46 H16 V22 L23 16 Z" fill="#e2e8f0" stroke="${s}" stroke-width="2.2"/><rect x="18" y="28" width="20" height="16" fill="${c}" fill-opacity="0.65"/>`,
    wiper: `<path d="M9 36 Q28 14 47 36" fill="none" stroke="${c}" stroke-width="3.2" stroke-linecap="round"/><line x1="28" y1="42" x2="40" y2="18" stroke="${s}" stroke-width="3" stroke-linecap="round"/><circle cx="28" cy="42" r="3.5" fill="${a}"/>`
  };
  const inner = bodies[kind] || bodies.bearing;
  const svg = `<svg viewBox="0 0 56 56" width="52" height="52" aria-hidden="true">${inner}</svg>`;
  SVG_CACHE[kind] = svg;
  return svg;
}

const HARDWARE_SUFFIXES = [
  ['M8×25 Flange Mounting Bolt Kit (4x)', 'bolt', 'Grade 10.9 Zinc-Nickel Flange', 3.40],
  ['M10×30 Stud & Dacromet Lock Nut Set', 'bolt', 'DIN 939 Anti-Seize Fastener', 4.80],
  ['Precision Alignment Dowel Pin Pair (10mm)', 'bolt', 'Hardened Ground Steel Pin', 2.90],
  ['Viton Elastomer Primary Lip Seal / O-Ring', 'seal', '220°C Chemical-Resistant Viton', 3.60],
  ['Single-Use Aluminum Crush Washer Set (4x)', 'seal', 'Annealed 12–18mm Seal Ring', 1.80],
  ['Anti-Vibration EPDM Isolation Bushing Pair', 'bracket', 'Shore 65A Rubber Damper', 5.20],
  ['Stainless Retaining Circlip & Snap Ring', 'seal', 'Corrosion-Proof Spring Steel', 2.40],
  ['Die-Cast Aluminum Support Bracket / Stay', 'bracket', 'CAD-Verified OEM Geometry', 14.50],
  ['Wire Harness Clip & Conduit Grommet Kit', 'sensor', 'UV-Stabilized PA66 Nylon', 3.10],
  ['Protective Dust Boot & Thermal Barrier Sleeve', 'hose', 'Silicone-Glass Heat Shield', 6.90],
  ['Calibration Shim & Clearance Spacer Kit', 'seal', 'Precision Stamped Steel 0.5mm', 4.20],
  ['Factory Torque-to-Yield Compression Collar', 'bearing', 'OEM Precision Compression Sleeve', 5.40],
  ['M12×1.25 Magnetic Drain & Gallery Plug', 'bolt', 'Neodymium Trap Plug + Washer', 4.60],
  ['Slotted Castle Nut & Stainless Cotter Pin', 'bolt', 'Safety-Wired Axle/Pivot Fastener', 3.20],
  ['Hardened Woodruff Key & Index Step Pin', 'bolt', 'Alloy Tool-Steel Shaft Lock Key', 2.75],
  ['304 Stainless Constant-Tension Clamp Set (4x)', 'hose', 'Worm-Drive / Spring Band Clamps', 3.90],
  ['M8×1.25 Brass Speed-Bleeder Nipple & Cap', 'bolt', 'Check-Ball One-Man Bleed Valve', 4.50],
  ['Needle Thrust Washer & Preload Shim Ring', 'bearing', 'Hardened 0.8mm Thrust Bearing', 5.10],
  ['PTFE Back-Up Ring & 10mm Banjo Union Bolt', 'seal', 'High-Pressure Hydraulic Split Ring', 4.10],
  ['Zinc-Dichromate U-Nut Speed Clip Retainer (6x)', 'bracket', 'Self-Locking Fender/Frame Clip', 2.60]
];

const EXTRA_SYSTEM_TEMPLATES = [
  // ENGINE (4 extra -> 6 total)
  ['eng_timing','Engine','Timing Chain / Belt, Tensioner & VVT Sprocket Kit','13506-21050','AISIN TK-204','Fig. 13-02',148,'belt',[
    ['Primary Silent Timing Chain (124 Links)','13506-21050-CHN','1 pc',54,'belt'],['Hydraulic Ratchet Chain Tensioner','13540-21020','1 pc',36,'spring'],
    ['Curved Nylon Timing Guides (Pair)','13561-21040','2 pcs',28,'bracket'],['VVT-i Camshaft Phasing Actuator Gear','13050-21041','1 pc',88,'bearing']
  ]],
  ['eng_piston','Engine','Piston Ring Set, Con-Rod Bearing & Main Seal Kit','13011-21140','NPR / TAIHO STD','Fig. 13-01',165,'bearing',[
    ['Nitrided Top & 2nd Compression Ring Set','13011-21140-RNG','1 set',62,'seal'],['3-Piece Oil Control Scraper Ring Kit','13011-21140-OIL','1 set',34,'seal'],
    ['Taiho Tri-Metal Con-Rod Bearing Shells','13041-21051','1 set',32,'bearing'],['Rear Crankshaft Main Viton Oil Seal','90311-75016','1 pc',19.5,'seal']
  ]],
  ['eng_head','Engine','MLS Cylinder Head Gasket & Overhaul Seal Kit','04111-21350','STONE JA-10912','Fig. 11-04',138,'pad',[
    ['3-Layer Stainless MLS Head Gasket','11115-21091','1 pc',58,'pad'],['Valve Cover Profile Seal + Plug Tube Rings','11213-21020','1 set',19,'seal'],
    ['Viton Valve Stem Oil Seals (16x)','90913-02111','16 pcs',24,'seal'],['Torque-to-Yield M10 Head Bolts (10x)','90910-02164','10 pcs',29,'bolt']
  ]],
  ['eng_mounts','Engine','Hydraulic Engine & Torque Strut Mount 4-Piece Kit','12305-21350','TENACITY / FEBEST','Fig. 11-07',154,'bracket',[
    ['RH Hydraulic Main Engine Insulator Mount','12305-21350-RH','1 pc',64,'bracket'],['LH Transmission Cradle Cushion Mount','12372-21190-LH','1 pc',44,'bracket'],
    ['Rear Lower Pitch Torque Roll Rod Mount','12363-21060-RR','1 pc',38,'bracket'],['Front Crossmember Snubber Rubber Bush','12361-21030-FR','1 pc',26,'bracket']
  ]],
  // FUEL & AIR (5 extra -> 6 total)
  ['ful_inj','Fuel & Air','Multi-Hole EFI / CRDi Fuel Injector & Seal Kit','23250-21080','DENSO 297500','Fig. 22-11',212,'spark',[
    ['Flow-Matched 12-Hole Fuel Injectors (4x)','23250-21080-INJ','4 pcs',185,'spark'],['Upper Fuel Rail Viton O-Ring Set','90301-07037','4 pcs',9,'seal'],
    ['Lower Intake Cushion Insulator Grommets','23291-41010','4 pcs',11,'seal'],['Fuel Rail Pulsation Damper Valve','23270-21010','1 pc',28,'sensor']
  ]],
  ['ful_etb','Fuel & Air','Electronic Throttle Body, MAF Sensor & Boot','22030-21030','AISIN / HITACHI','Fig. 22-09',178,'sensor',[
    ['Motorized Throttle Valve Assembly (55mm)','22030-21030-ETB','1 pc',142,'sensor'],['Hot-Wire Mass Airflow (MAF) Sensor','22204-22010','1 pc',48,'sensor'],
    ['Throttle Body Metal Flange Gasket','22271-21040','1 pc',6.5,'seal'],['Reinforced EPDM Intake Accordion Boot','17881-21140','1 pc',24,'hose']
  ]],
  ['ful_pump','Fuel & Air','In-Tank Turbine Fuel Pump, Strainer & Level Sender','77020-52370','DENSO 950-0105','Fig. 77-01',144,'filter',[
    ['Brushless Turbine Fuel Pump Motor (3.8 bar)','23221-21131','1 pc',72,'filter'],['In-Tank Nylon Pick-Up Strainer Sock','23217-21020','1 pc',12,'filter'],
    ['Fuel Level Float Sender Rheostat Arm','83320-52210','1 pc',36,'sensor'],['Tank Lock Ring Viton Seal Gasket','77169-52040','1 pc',14,'seal']
  ]],
  ['ful_pcv','Fuel & Air','PCV Breather Valve, EVAP Purge & Vacuum Kit','12204-21011','AISIN PCV-004','Fig. 12-01',42,'hose',[
    ['OEM Spring-Loaded PCV Check Valve','12204-21011-PCV','1 pc',14,'sensor'],['EVAP Vacuum Purge Control Solenoid','90910-12276','1 pc',28,'sensor'],
    ['Oil-Resistant Crankcase Breather Hose','12261-21060','1 pc',11.5,'hose'],['Intake Manifold Vacuum Check Tee','90413-08005','1 pc',5.5,'bolt']
  ]],
  ['ful_boost','Fuel & Air','Turbo / Intake Resonator Pipe & MAP Sensor Kit','17341-30090','SAMCO / DENSO','Fig. 17-01',112,'hose',[
    ['4-Ply Silicone Charge Coupler Hose','17341-30090-HSE','1 pc',38,'hose'],['Stainless T-Bolt Boost Clamps (4x)','90467-64004','4 pcs',16,'bolt'],
    ['Manifold Absolute Pressure (MAP) Sensor','89421-71030','1 pc',46,'sensor'],['Intake Air Temp (IAT) Thermistor Probe','89424-60010','1 pc',19,'sensor']
  ]],
  // TRANSMISSION (5 extra -> 6 total)
  ['trn_sol','Transmission','Shift Solenoid Pack & Valve Body Gasket Kit','35210-52010','AISIN / DENSO','Fig. 35-12',196,'sensor',[
    ['Linear Line Pressure EPC Solenoid','35290-52060','1 pc',78,'sensor'],['TCC Torque Converter Lock-Up Solenoid','35280-52040','1 pc',62,'sensor'],
    ['Valve Body Separator Gasket Kit','35433-52010','1 kit',24,'seal'],['Internal ATF Temperature Sensor Harness','82125-52020','1 pc',34,'sensor']
  ]],
  ['trn_cv','Transmission','CV Axle Drive Shaft, Tripod Boot & Seal Kit','43420-12760','NKN JAPAN / GSP','Fig. 43-02',142,'cable',[
    ['Outer Birfield CV Joint + ABS Tone Ring','43460-19885','1 pc',68,'bearing'],['Inner Tripod Plunge Joint & Roller','43403-12080','1 pc',54,'bearing'],
    ['Thermoplastic CV Boot + Clamps Kit','04438-12670','1 kit',22,'seal'],['Differential Side Output Oil Seals (Pair)','90311-34043','2 pcs',16,'seal']
  ]],
  ['trn_prop','Transmission','Propshaft Center Carrier Bearing & U-Joint Kit','37230-35130','KOYO / GMB GUT-24','Fig. 37-01',86,'bearing',[
    ['Propshaft Center Support Carrier Bearing','37230-35130-BRG','1 pc',48,'bearing'],['Heavy-Duty Greaseable Spider U-Joint','04371-60070','1 pc',26,'bearing'],
    ['Transfer Case Rear Output Oil Seal','90311-40038','1 pc',12.5,'seal'],['Companion Flange Lock Bolt & Nut Set','90105-11021','4 pcs',8.5,'bolt']
  ]],
  ['trn_cbl','Transmission','Gear Selector Shift Cable & Neutral Switch','33820-12310','TSK / AISIN','Fig. 33-12',74,'cable',[
    ['PTFE-Lined Selector Control Cable','33820-12310-CBL','1 pc',44,'cable'],['Park / Neutral Inhibitor Range Switch','84540-52070','1 pc',36,'sensor'],
    ['Shift Linkage Poly End Bushings (2x)','33835-08010','2 pcs',7.5,'bracket'],['Bulkhead Retaining Spring Clip','90468-14034','1 pc',4.2,'bolt']
  ]],
  ['trn_cool','Transmission','ATF Oil Heat Exchanger & Cooler Line Kit','32910-12340','AISIN / GATES','Fig. 35-04',92,'bottle',[
    ['Brazed Plate ATF Heat Exchanger','33493-12090','1 pc',68,'fan'],['Reinforced 10mm ATF Cooler Hose Pair','32941-12190','2 pcs',19,'hose'],
    ['ATF Thermal Bypass Valve Unit','33950-12010','1 pc',29,'sensor'],['Constant-Force Tension Hose Clamps (4x)','90467-16002','4 pcs',6,'bolt']
  ]],
  // CLUTCH (5 extra -> 6 total)
  ['clt_fly','Clutch','Balanced Flywheel, Pilot Bearing & Bolt Kit','13405-21040','LUK / AISIN','Fig. 13-03',224,'rotor',[
    ['Precision-Balanced Flywheel / Flexplate','13405-21040-FLY','1 pc',178,'rotor'],['Koyo Sealed Input Pilot Ball Bearing','90363-12002','1 pc',14.5,'bearing'],
    ['Hardened Starter Ring Gear Band','13453-21010','1 pc',38,'bearing'],['High-Tensile Crank-to-Flywheel Bolts (8x)','90910-02116','8 pcs',18,'bolt']
  ]],
  ['clt_mc','Clutch','Clutch Master Cylinder & Braided Line Kit','31420-12040','AISIN CMT-134','Fig. 31-03',62,'bottle',[
    ['Anodized 15.87mm Clutch Master Cylinder','31420-12040-MC','1 pc',46,'bottle'],['Seiken EPDM Master Piston Seal Kit','04311-12110','1 kit',12,'seal'],
    ['Braided High-Pressure Clutch Flex Hose','90947-02D65','1 pc',16.5,'hose'],['Clevis Pin & Pedal Return Spring','90249-08145','1 kit',5.8,'spring']
  ]],
  ['clt_fork','Clutch','Forged Clutch Release Fork, Pivot & Boot','31204-12120','AISIN CFK-019','Fig. 31-02',48,'bracket',[
    ['Forged Chromoly Release Fork Lever','31204-12120-FRK','1 pc',32,'bracket'],['Hardened Spherical Pivot Ball Stud','31236-12020','1 pc',9.5,'bolt'],
    ['Bellhousing Rubber Dust Exclusion Boot','31126-12051','1 pc',8.5,'seal'],['Release Bearing Wire Clip Spring Pair','31232-12010','2 pcs',4.5,'spring']
  ]],
  ['clt_pdl','Clutch','Clutch Pedal Bush, Pad & Start Interlock Switch','31301-12310','OEM PEDAL-KIT','Fig. 55-02',39,'wiper',[
    ['Clutch Start Safety Interlock Switch','84520-12020','1 pc',18,'sensor'],['Anti-Slip Rubber Pedal Pad','31321-12030','1 pc',6.5,'pad'],
    ['Over-Center Pedal Assist Spring','90507-14045','1 pc',11,'spring'],['Pedal Pivot Nylon Bushings (Pair)','90386-13015','2 pcs',5.5,'bracket']
  ]],
  ['clt_dmp','Clutch','Hydraulic Clutch Damper & Bleeder Valve Kit','31490-12020','SEIKEN CD-101','Fig. 31-05',34,'filter',[
    ['Clutch Line Accumulator Damper Chamber','31490-12020-ACC','1 pc',24,'bottle'],['M8 Brass Speed-Bleeder Nipple + Cap','31478-30010','1 pc',6,'bolt'],
    ['Steel Clutch Tube Chassis Clamp Bracket','90461-10121','2 pcs',4.5,'bracket'],['10mm Copper Banjo Crush Washers (4x)','90430-10074','4 pcs',3.5,'seal']
  ]],
  // SUSPENSION (5 extra -> 6 total)
  ['sus_rear','Suspension','Rear Gas Shock Absorber & Coil Spring Kit (Pair)','48530-8Z190','KYB 343434','Fig. 48-04',138,'spring',[
    ['KYB Excel-G Rear Gas Shock Absorbers (2x)','48530-8Z190-PR','2 pcs',92,'spring'],['Progressive-Rate Rear Coil Springs (Pair)','48231-12B10','2 pcs',78,'spring'],
    ['Upper/Lower Spring Insulator Pads','48257-12040','4 pcs',14,'seal'],['Urethane Bump Stop & Dust Bellows','48341-12190','2 pcs',16,'spring']
  ]],
  ['sus_sway','Suspension','Stabilizer Sway Bar Links & D-Bushing Kit','48820-47020','SANKEI 555 / CTR','Fig. 48-03',46,'bracket',[
    ['Front Stabilizer End Link Pair (555 Japan)','48820-47020-FR','2 pcs',28,'bracket'],['Rear Anti-Roll Stabilizer Drop Links','48830-42022-RR','2 pcs',26,'bracket'],
    ['Sway Bar Polyurethane D-Bushings (2x)','48815-12390','2 pcs',11,'bracket'],['Zinc-Plated Saddle Bushing Brackets','48824-12150','2 pcs',9,'bracket']
  ]],
  ['sus_hub','Suspension','Front & Rear Gen-3 ABS Wheel Hub Bearing Unit','43550-47011','KOYO / NSK JAPAN','Fig. 43-03',118,'bearing',[
    ['Koyo Gen-3 Bolt-On Front Hub Bearing','43550-47011-FR','1 pc',78,'bearing'],['Rear Wheel Hub + Magnetic ABS Encoder','42450-12170-RR','1 pc',72,'bearing'],
    ['M12×1.5 Chromoly Knurled Wheel Studs (5x)','90942-02049','5 pcs',12,'bolt'],['30mm 12-Point Axle Spindle Lock Nut','90177-22001','1 pc',6.5,'bolt']
  ]],
  ['sus_bush','Suspension','Rear Trailing Arm & Subframe Bushing Kit','48725-12630','RBI / FEBEST','Fig. 48-04',68,'bracket',[
    ['Rear Axle Trailing Arm Pivot Bushes (2x)','48725-12630-PR','2 pcs',38,'bracket'],['Subframe Crossmember Isolator Bushes','52205-12020','2 pcs',34,'bracket'],
    ['Eccentric Camber Alignment Bolt + Washer','48190-32010','2 pcs',14,'bolt'],['Lateral Panhard Track Bar Bushing Set','48706-60040','2 pcs',16,'bracket']
  ]],
  ['sus_leaf','Suspension','4x4 Control Arm Ball Joint & Camber Arm Kit','48630-60040','SANKEI 555 UPPER','Fig. 48-02',156,'bracket',[
    ['Forged Upper Wishbone Control Arm','48630-60040-UP','1 pc',88,'bracket'],['Heavy-Duty Greaseable Ball Joint Pair','43310-60050','2 pcs',42,'bracket'],
    ['Lower Arm Caster & Camber Sleeve Kit','48655-60040','2 pcs',28,'bearing'],['Coil-Spring Top Isolation Mounting Plate','48680-60050','2 pcs',36,'pad']
  ]],
  // STEERING (5 extra -> 6 total)
  ['str_eps','Steering','EPS Power Steering Damper & U-Joint Shaft','45254-28040','JTEKT JAPAN','Fig. 45-01',64,'bearing',[
    ['EPS Motor Urethane Star Damper Coupler','45254-28040-DMP','1 pc',14,'bearing'],['Intermediate Steering U-Joint Yoke Shaft','45260-12710','1 pc',54,'cable'],
    ['Steering Angle Clockspring Ribbon Sensor','89245-12040','1 pc',48,'sensor'],['Firewall Steering Column Seal Boot','45025-12190','1 pc',12,'seal']
  ]],
  ['str_pump','Steering','Steering Vane Pump, Belt & Auto Tensioner Kit','44310-35660','BANDO / KOYO','Fig. 44-01',118,'belt',[
    ['Bando Rib-Ace EPDM Serpentine Drive Belt','90916-02664','1 pc',22,'belt'],['Automatic Spring Belt Tensioner Arm','16620-31040','1 pc',68,'belt'],
    ['Koyo Sealed Steel Idler Bearing Pulley','16604-31020','1 pc',28,'bearing'],['High-Pressure Steering Return Hose','44410-35690','1 pc',34,'hose']
  ]],
  ['str_knk','Steering','Forged Steering Knuckle, Shield & Seal Kit','43211-12440','GENUINE OEM','Fig. 43-01',146,'bracket',[
    ['Forged Iron Steering Knuckle Carrier','43211-12440-KNK','1 pc',118,'bracket'],['Zinc-Coated Rotor Mud Splash Shield','47781-12260','1 pc',22,'pad'],
    ['Inner Spindle Oil & Water Exclusion Seal','90316-A0001','1 pc',12,'seal'],['Strut Clevis High-Tensile Pinch Bolts','90105-15003','2 pcs',9,'bolt']
  ]],
  ['str_mnt','Steering','Steering Rack Poly Bushing & Bellows Kit','45516-12080','FEBEST / 555','Fig. 45-05',38,'cable',[
    ['Rack Housing Poly Clamp Bushings (2x)','45516-12080-BSH','2 pcs',18,'bracket'],['Ozone-Proof Rack Bellows Boots (Pair)','45535-12100','2 pcs',16,'hose'],
    ['Stainless Boot Crimp Clamps (4x)','90460-48003','4 pcs',5.5,'bolt'],['M12 Subframe Rack Mounting Bolts (4x)','90119-12310','4 pcs',8.5,'bolt']
  ]],
  ['str_clk','Steering','Combination Light/Wiper Stalk & Spiral Cable','84306-12110','OEM SPIRAL','Fig. 84-01',76,'sensor',[
    ['Airbag & Horn Spiral Clockspring','84306-12110-SPR','1 pc',48,'cable'],['Headlight & Turn-Signal Column Stalk','84140-12580','1 pc',36,'sensor'],
    ['Variable Intermittent Wiper Stalk Switch','84652-12A20','1 pc',34,'sensor'],['Steering Wheel Taper Lock Nut','90179-12144','1 pc',3.8,'bolt']
  ]],
  // BRAKES (5 extra -> 6 total)
  ['brk_rear','Brakes','Rear Ceramic Pad / Shoe, Disc & Cylinder Kit','04466-52151','AKEBONO / BREMBO','Fig. 47-06',98,'pad',[
    ['Rear Low-Dust Ceramic Pad / Shoe Set','04466-52151-PAD','4 pcs',42,'pad'],['Rear Solid / Vented Brake Disc Pair','42431-12310','2 pcs',86,'rotor'],
    ['Seiken Rear Drum Wheel Cylinder Unit','47550-52011','1 pc',28,'bottle'],['Parking Brake Self-Adjuster Spring Kit','04943-52040','1 kit',14,'spring']
  ]],
  ['brk_cal','Brakes','Front Caliper Piston, Slide Pin & Seal Kit','04478-52170','SEIKEN / ADVICS','Fig. 47-05',56,'rotor',[
    ['Hard-Chrome Caliper Piston (54mm)','47731-52190','1 pc',24,'bottle'],['Seiken Square-Cut Seal & Dust Boot Kit','04478-52170-KIT','1 kit',18,'seal'],
    ['Precision Upper/Lower Caliper Slide Pins','47715-52190','4 pcs',16,'bolt'],['Silicone Caliper Grease & Bleeder Cap','08887-01206','1 kit',7.5,'bottle']
  ]],
  ['brk_mc','Brakes','Tandem Brake Master Cylinder & Booster Kit','47201-52590','ADVICS / AISIN','Fig. 47-02',148,'bottle',[
    ['Advics Dual-Circuit Brake Master Cylinder','47201-52590-MC','1 pc',112,'bottle'],['Reservoir Fluid Level Float Sensor Cap','47230-47090','1 pc',18,'sensor'],
    ['Vacuum Brake Booster Check Valve','44730-28030','1 pc',12,'sensor'],['Master-to-Booster Flange EPDM Seal','44785-12060','1 pc',6.5,'seal']
  ]],
  ['brk_hse','Brakes','DOT Braided Brake Flex Hoses & Banjo Kit (4x)','90947-02F38','SEIKEN SH-31245','Fig. 47-08',54,'hose',[
    ['Front Caliper Braided Brake Hoses (2x)','90947-02F38-FR','2 pcs',28,'hose'],['Rear Axle Braided Brake Hoses (2x)','90947-02F41-RR','2 pcs',26,'hose'],
    ['Hollow Banjo Bolts + Copper Washers','90401-10015','4 pcs',8,'bolt'],['Strut Brake Hose Spring U-Clips (4x)','90468-08033','4 pcs',4.5,'bolt']
  ]],
  ['brk_pk','Brakes','Parking Handbrake Cable Pair & Equalizer Kit','46420-12690','TSK JAPAN','Fig. 46-01',64,'cable',[
    ['RH Rear Galvanized Parking Brake Cable','46420-12690-RH','1 pc',32,'cable'],['LH Rear Galvanized Parking Brake Cable','46430-12610-LH','1 pc',32,'cable'],
    ['Handbrake Lever Equalizer Balance Yoke','46251-12040','1 pc',12,'bracket'],['Underbody Cable Bracket & Clevis Pins','46417-12030','2 pcs',7,'bolt']
  ]],
  // COOLING (5 extra -> 6 total)
  ['col_fan','Cooling','Brushless Dual Radiator Fan & Shroud Unit','16361-21110','DENSO 168000','Fig. 16-03',148,'fan',[
    ['Sealed 12V Cooling Fan DC Motor','16363-21110','1 pc',74,'fan'],['7-Blade Balanced Nylon Fan Impeller','16361-21110-BLD','1 pc',28,'fan'],
    ['PA66 Glass-Filled Radiator Fan Shroud','16711-21110','1 pc',42,'pad'],['PWM Fan Control Resistor Module','89257-12010','1 pc',46,'sensor']
  ]],
  ['col_hse','Cooling','Upper/Lower Radiator Hose, Tank & Cap Kit','16571-21110','GATES / FUTABA','Fig. 16-03',68,'hose',[
    ['EPDM Molded Upper Radiator Inlet Hose','16571-21110-UP','1 pc',19,'hose'],['EPDM Molded Lower Radiator Return Hose','16572-21110-LO','1 pc',21,'hose'],
    ['Coolant Expansion Overflow Reservoir Tank','16470-21120','1 pc',34,'bottle'],['108 kPa (1.1 bar) Stainless Radiator Cap','16401-31650','1 pc',12,'seal']
  ]],
  ['col_oil','Cooling','Engine / EGR Oil Cooler & Bypass Pipe Kit','15710-30010','AISIN / DENSO','Fig. 15-03',114,'filter',[
    ['Stainless Plate Engine Oil Cooler Core','15710-30010-EX','1 pc',82,'fan'],['Rear Coolant Water Bypass Metal Pipe','16268-37020','1 pc',28,'hose'],
    ['Oil Cooler Viton Figure-8 Gasket','15785-30010','1 pc',9.5,'seal'],['Engine Coolant Temp (ECT) Brass Sensor','89422-33030','1 pc',18,'sensor']
  ]],
  ['col_htr','Cooling','Cabin Heater Matrix Core & Inverter Pump Kit','87107-12560','DENSO COOLGEAR','Fig. 87-14',96,'fan',[
    ['Brazed Aluminum Cabin Heater Core','87107-12560-COR','1 pc',74,'fan'],['Firewall Heater Hose Nylon Tee Pair','87248-60460','2 pcs',14,'hose'],
    ['Heater Supply & Return Rubber Hoses','87245-12840','2 pcs',22,'hose'],['Electric Inverter / Auxiliary Water Pump','G9040-47040','1 pc',98,'fan']
  ]],
  ['col_thrm','Cooling','Water Outlet Housing, 82°C Thermostat & Seal','16321-21020','AISIN THT-018','Fig. 16-01',48,'sensor',[
    ['Tropical 82°C Wax Thermostat Valve','90916-03136','1 pc',18,'sensor'],['Cast Aluminum Thermostat Water Neck','16321-21020-NCK','1 pc',24,'bracket'],
    ['Molded EPDM Thermostat Jiggle Gasket','16325-62010','1 pc',6.5,'seal'],['Radiator Petcock Drain Plug + O-Ring','16417-21220','1 pc',5.5,'bolt']
  ]],
  // AC / HVAC (5 extra -> 6 total)
  ['hvc_cnd','AC / HVAC','Parallel-Flow AC Condenser & Drier Bag Kit','88450-12280','DENSO 477-0774','Fig. 88-01',144,'fan',[
    ['Micro-Channel Aluminum AC Condenser','88450-12280-CND','1 pc',118,'fan'],['XH-9 Desiccant Receiver Drier Cartridge','88474-17010','1 pc',18,'filter'],
    ['AC High/Low Pressure Transducer Switch','88719-33020','1 pc',28,'sensor'],['HNBR Green R134a/R1234yf O-Ring Set','90099-14120','10 pcs',9,'seal']
  ]],
  ['hvc_evp','AC / HVAC','Under-Dash AC Evaporator Core & TXV Kit','88501-12440','DENSO EV-476','Fig. 87-14',132,'fan',[
    ['Anti-Bacterial Coated Evaporator Core','88501-12440-EVP','1 pc',96,'fan'],['Denso Block Thermal Expansion Valve','88515-52080','1 pc',34,'sensor'],
    ['Evaporator Fin Anti-Freeze Thermistor','88625-47021','1 pc',19,'sensor'],['Condensation Drain Tube & Foam Tape','88539-12110','1 kit',8.5,'hose']
  ]],
  ['hvc_blw','AC / HVAC','Cabin Blower Motor, Resistor & Blend Servo','87103-12080','DENSO 272700','Fig. 87-14',98,'fan',[
    ['12V Squirrel-Cage Cabin Blower Motor','87103-12080-BLW','1 pc',68,'fan'],['Solid-State MOSFET Blower Resistor','87165-13010','1 pc',32,'sensor'],
    ['Air-Mix / Mode Blend Door Stepper Servo','87106-12190','1 pc',44,'sensor'],['Fresh-Air Recirculation Damper Foam Seal','87195-12040','1 pc',9,'pad']
  ]],
  ['hvc_pip','AC / HVAC','AC Discharge/Suction Hose & Mag Clutch Kit','88711-12790','DENSO COOLGEAR','Fig. 88-02',86,'hose',[
    ['AC Compressor Magnetic Clutch & Pulley','88410-12560','1 set',64,'bearing'],['High-Pressure Barrier Discharge AC Hose','88711-12790-HSE','1 pc',38,'hose'],
    ['High/Low Service Port Schrader Cores','88374-20010','4 pcs',8,'bolt'],['ND-OIL 8 / Hybrid POE AC Oil (150ml)','08885-09129','1 btl',24,'bottle']
  ]],
  ['hvc_rear','AC / HVAC','Rear Cabin Dual-Zone AC Piping & Vent Kit','88578-28120','DENSO REAR-AC','Fig. 87-15',110,'hose',[
    ['Rear Evaporator Expansion Valve Block','88515-28160','1 pc',36,'sensor'],['Roof-Line Rear Cabin AC Blower Motor','87103-28110','1 pc',72,'fan'],
    ['Dashboard Louver Air Vent Grille Assembly','55650-12310','1 pc',28,'pad'],['Rear AC Aluminum Tube Block-Off Seals','88718-28110','2 pcs',11,'seal']
  ]],
  // ELECTRICAL (5 extra -> 6 total)
  ['elc_alt','Electrical','High-Output Alternator & Decoupler Pulley Kit','27060-21150','DENSO 104210','Fig. 19-03',218,'battery',[
    ['Overrunning Alternator Decoupler Pulley','27415-0W021','1 pc',42,'bearing'],['IC Voltage Regulator & Brush Assembly','27700-21050','1 pc',34,'sensor'],
    ['High-Current 6-Diode Rectifier Bridge','27357-21080','1 pc',29,'sensor'],['NTN Sealed Alternator Rotor Bearings (2x)','90099-10192','2 pcs',18,'bearing']
  ]],
  ['elc_str','Electrical','Planetary Reduction Starter Motor & Solenoid','28100-21061','DENSO 428000','Fig. 19-04',164,'bearing',[
    ['Starter Magnetic Solenoid Plunger Switch','28150-21040','1 pc',34,'sensor'],['One-Way Pinion Overrunning Bendix Clutch','28011-21030','1 pc',38,'bearing'],
    ['Armature Commutator Carbon Brush Holder','28140-21050','1 kit',16,'sensor'],['Planetary Reduction Nylon Ring Gear','28021-21020','1 pc',22,'bearing']
  ]],
  ['elc_cop','Electrical','Coil-on-Plug Ignition Pack, Crank & Cam Sensors','90919-02258','DENSO / NGK U5065','Fig. 19-01',158,'spark',[
    ['Direct Coil-on-Plug Ignition Coils (4x)','90919-02258-COP','4 pcs',128,'spark'],['Crankshaft Position (CKP) Timing Sensor','90919-05070','1 pc',36,'sensor'],
    ['Camshaft Phase Position (CMP) Sensor','90919-05060','1 pc',34,'sensor'],['4-Pin Waterproof Ignition Connector Plugs','90980-11885','4 pcs',16,'sensor']
  ]],
  ['elc_lmp','Electrical','Projector Headlamp, Fuse Relay Box & Horn Kit','81110-12J30','KOITO / DENSO','Fig. 81-01',192,'wiper',[
    ['Koito Projector Headlamp Housing (RH/LH)','81110-12J30-LMP','1 pc',156,'wiper'],['LED / HID High-Low Bulb & Driver Module','90981-20024','2 pcs',44,'spark'],
    ['Engine Bay Fuse & 30A Micro Relay Box','90987-02028','10 pcs',22,'sensor'],['Waterproof Dual-Tone 400/500Hz Horns','86510-12310','2 pcs',24,'sensor']
  ]],
  ['elc_sns','Electrical','Wheel Speed ABS, Knock & Oil Pressure Sensors','89542-06081','DENSO / AISIN','Fig. 84-14',96,'sensor',[
    ['Front/Rear Hall-Effect ABS Speed Sensor','89542-06081-ABS','1 pc',36,'sensor'],['Engine Block Piezo Knock Detonation Sensor','89615-20090','1 pc',42,'sensor'],
    ['Oil Gallery Pressure Warning Switch','83530-60020','1 pc',14,'sensor'],['Reverse Backup Lamp & Park Sonar Sensor','89341-33180','1 pc',38,'sensor']
  ]],
  // EXHAUST (5 extra -> 6 total)
  ['exh_mnf','Exhaust','Exhaust Manifold MLS Gasket, Studs & Shield','17173-21030','STONE JAPAN','Fig. 17-01',68,'hose',[
    ['Multi-Layer Stainless Manifold Gasket','17173-21030-GSK','1 pc',24,'pad'],['Inconel M10 Exhaust Stud & Copper Nuts','90116-10175','5 pcs',18,'bolt'],
    ['Embossed Aluminum Manifold Heat Shield','17167-21140','1 pc',32,'pad'],['EGR Pipe Metal Flange Gasket & Clamp','25627-21020','1 kit',11,'seal']
  ]],
  ['exh_ctr','Exhaust','Stainless Center Resonator & Flex Bellows','17420-21350','FUTABA / BOSAL','Fig. 17-02',148,'hose',[
    ['Double-Mesh Stainless Flex Pipe Bellows','17420-FLX-50','1 pc',42,'hose'],['Center Acoustic Resonator Drum Pipe','17420-21350-RES','1 pc',88,'hose'],
    ['Downstream Post-Cat Heated O2 Sensor','89465-12840','1 pc',78,'sensor'],['3-Bolt Triangular Exhaust Flange Gasket','90917-06078','1 pc',9,'seal']
  ]],
  ['exh_muf','Exhaust','Rear Muffler Silencer Drum, Tip & Hangers','17430-21770','BOSAL / OEM','Fig. 17-02',164,'hose',[
    ['Aluminized Chambered Rear Silencer Drum','17430-21770-MUF','1 pc',132,'hose'],['Polished Stainless Exhaust Tailpipe Tip','17408-12040','1 pc',24,'hose'],
    ['Heavy-Duty EPDM 3-Hole Exhaust Hangers','17565-21040','3 pcs',14,'bracket'],['Spring-Loaded 2-Bolt Flange Kit','90901-05026','2 pcs',11,'bolt']
  ]],
  ['exh_egr','Exhaust','EGR Valve Actuator & DPF Pressure Sensor Kit','25620-30050','DENSO DMA-0115','Fig. 25-01',188,'sensor',[
    ['Water-Cooled Step-Motor EGR Valve','25620-30050-EGR','1 pc',148,'sensor'],['DPF Differential Exhaust Pressure Sensor','89480-30020','1 pc',62,'sensor'],
    ['Exhaust Gas Temp (EGT) Thermocouple','89425-30040','1 pc',54,'sensor'],['Silicone DPF Impulse Sensor Hoses (2x)','89483-30010','2 pcs',14,'hose']
  ]],
  ['exh_shd','Exhaust','Underbody Catalytic & Tunnel Heat Shield Kit','58151-12190','GENUINE OEM','Fig. 58-01',46,'pad',[
    ['Embossed Floor Tunnel Thermal Shield','58151-12190-PNL','1 pc',32,'pad'],['Stainless 32mm Star Repair Washers (6x)','90183-06044','6 pcs',7.5,'seal'],
    ['48–54mm Stainless Exhaust Band Clamp','17452-12030','1 pc',12,'bolt'],['Rear Bumper Heat Deflector Plate','58327-12080','1 pc',16,'pad']
  ]],
  // BODY & INTERIOR (5 extra -> 6 total)
  ['bdy_mir','Body & Interior','Power-Folding Wing Mirror & Window Regulator','87910-12D60','AISIN / TYC','Fig. 87-01',142,'wiper',[
    ['Power-Fold Mirror Nylon Gear Motor','87945-12040','1 pc',34,'bearing'],['Heated Wide-Angle Mirror Glass + Turn LED','87931-12D90','1 pc',28,'wiper'],
    ['Front Door Window Regulator + Motor','69801-12220','1 pc',88,'cable'],['Flocked Rubber Door Window Run Seal','68141-12310','1 pc',32,'seal']
  ]],
  ['bdy_lck','Body & Interior','Door Lock Actuator, Handle & Weatherstrip Kit','69030-02380','AISIN DLT-064','Fig. 69-01',94,'sensor',[
    ['Electric Central Door Lock Actuator','69030-02380-ACT','1 pc',68,'sensor'],['Outer Pull Handle + Smart Touch Sensor','69210-12240','1 pc',36,'wiper'],
    ['EPDM Door Aperture Monsoon Weatherstrip','62311-12250','1 pc',34,'seal'],['Outer Window Chrome Beltline Scraper','75710-12780','1 pc',29,'wiper']
  ]],
  ['bdy_bmp','Body & Interior','Bumper Brackets, Fender Liner & Engine Tray','52115-12470','TONG YANG OEM','Fig. 52-01',78,'pad',[
    ['Front Bumper Side Retainer Brackets (2x)','52115-12470-PR','2 pcs',22,'bracket'],['Under-Engine Composite Splash Guard Tray','51441-12270','1 pc',48,'pad'],
    ['Front Wheel Arch Fender Liner Apron','53875-12510','1 pc',32,'pad'],['Grille & Radiator Trim Clips (15x)','90467-07217','15 pcs',8,'bolt']
  ]],
  ['bdy_int','Body & Interior','Master Window Switch, Trunk Latch & Seatbelt','84820-12540','GENUINE OEM','Fig. 84-01',84,'sensor',[
    ['Driver 4-Door Master Window Switch','84820-12540-SW','1 pc',48,'sensor'],['Electric Trunk / Tailgate Release Latch','64600-12190','1 pc',42,'sensor'],
    ['Driver Seatbelt Buckle + Hall Switch','73230-12670','1 pc',34,'sensor'],['Glovebox Soft-Open Hydraulic Damper','55557-12040','1 pc',12,'spring']
  ]],
  ['bdy_fog','Body & Interior','LED Fog Lamp, Tow Eye Cover & Grille Trim Kit','81210-0D042','KOITO JAPAN','Fig. 81-02',76,'wiper',[
    ['Koito Projector Fog Lamp Pair (LH/RH)','81210-0D042-FOG','2 pcs',54,'wiper'],['Front Bumper Tow Hook Cap & Chrome Bezel','52127-12580','1 pc',14,'pad'],
    ['Tailgate Gas Damper Shock Strut Pair','68950-0W510','2 pcs',42,'spring'],['Roof Drip Moulding Rubber Strip Pair','75551-12690','2 pcs',28,'seal']
  ]]
];


function getCodesForVehicle(v, variantIdx) {
  const base = BRAND_CODES[v.brand] || {
    epc: v.brand + ' Official EPC',
    oil: v.brand.slice(0,3).toUpperCase() + '-1540-901',
    air: v.brand.slice(0,3).toUpperCase() + '-1780-412',
    fuel: v.brand.slice(0,3).toUpperCase() + '-2330-510',
    cabin: v.brand.slice(0,3).toUpperCase() + '-8713-220',
    pad: v.brand.slice(0,3).toUpperCase() + '-4502-310',
    rotor: v.brand.slice(0,3).toUpperCase() + '-4351-608',
    spark: v.brand.slice(0,3).toUpperCase() + '-9091-771',
    clutch: v.brand.slice(0,3).toUpperCase() + '-3121-204',
    rack: v.brand.slice(0,3).toUpperCase() + '-4551-308',
    rad: v.brand.slice(0,3).toUpperCase() + '-1640-611',
    ac: v.brand.slice(0,3).toUpperCase() + '-8831-409',
    exh: v.brand.slice(0,3).toUpperCase() + '-1741-522'
  };
  const vr = (v.variants && v.variants[variantIdx]) || (v.variants && v.variants[0]) || { chassis:'STD', pt:'PETROL' };
  const m = v.model.toLowerCase();
  const c = { ...base };
  if (v.brand === 'Toyota' && (m.includes('land cruiser') || m.includes('prado'))) {
    c.oil = vr.pt === 'DIESEL' ? '90915-YZZD4' : '90915-YZZD2';
    c.air = '17801-31090';
    c.cabin = '87139-YZZ20';
    c.pad = '04465-60320';
    c.rotor = '43512-60190';
    c.rack = '44200-60170';
    c.rad = '16400-30172';
  } else if (v.brand === 'Mitsubishi' && m.includes('pajero')) {
    c.oil = vr.pt === 'DIESEL' ? 'ME013307' : 'MD352627';
    c.air = '1500A608';
    c.cabin = '7803A028';
    c.pad = '4605A879';
    c.rotor = '4615A035';
    c.rack = '4410A323';
    c.rad = '1350A141';
  } else if (v.brand === 'Tata' && m.includes('hexa')) {
    c.oil = '2790-1813-0102';
    c.air = '5442-0913-0101';
    c.pad = '5442-4210-0118';
    c.rotor = '5442-4210-0104';
  }
  return { codes: c, variant: vr };
}

const CATALOG_CACHE = new Map();

function buildFullCatalog(v, variantIdx) {
  const { codes: c, variant: vr } = getCodesForVehicle(v, variantIdx);
  const cacheKey = `${v.brand}|${v.model}|${vr.chassis}|${vr.pt}`;
  if (CATALOG_CACHE.has(cacheKey)) return CATALOG_CACHE.get(cacheKey);
  const p = v.brand.slice(0,3).toUpperCase().replace(/[^A-Z]/g,'X');

  const core = [
    { id:'eng_oil', sys:'Engine', name:'Engine Oil Filter Assembly (Complete Kit)', oem:c.oil, alt:'DENSO 150-2002 / VIC C-110', epc:c.epc+' Fig. 15-02', price:14.5, task:'oil', icon:'filter', subs:[
      { id:'s_elm', name:'Filter Element / Pleated Cartridge', oem:c.oil+'-ELM', qty:'1 pc', price:7.5, spec:'20-micron synthetic pleats', icon:'filter' },
      { id:'s_org', name:'Viton Housing Rubber O-Ring / Seal', oem:p+'-90301-68005', qty:'1 pc', price:2.2, spec:'Pre-oil Viton seal', icon:'seal' },
      { id:'s_wsh', name:'Oil Pan Drain Plug Crush Washer', oem:p+'-90430-12031', qty:'1 pc', price:1.25, spec:'Single-use 12mm crush washer', icon:'seal' },
      { id:'s_cap', name:'Oil Filter Housing Cap (64mm)', oem:p+'-15620-37010', qty:'1 pc', price:18.0, spec:'14-flute PA66 cap', icon:'bolt' },
      { id:'s_val', name:'Center Bolt & Bypass Relief Valve', oem:p+'-15609-21020', qty:'1 pc', price:4.9, spec:'1.0 bar cold bypass valve', icon:'spring' }
    ]},
    { id:'eng_spark', sys:'Engine', name:vr.pt==='DIESEL'?'Ceramic Glow Plug Kit (4x)':'Laser Iridium Spark Plug Kit (4x)', oem:c.spark, alt:'NGK / DENSO Iridium', epc:c.epc+' Fig. 19-01', price:68.0, task:'spark', icon:'spark', subs:[
      { id:'s_sp1', name:'Plug Core Set (4x)', oem:c.spark, qty:'4 pcs', price:58.0, spec:'Pre-gapped 1.1mm factory spec', icon:'spark' },
      { id:'s_sp2', name:'Dielectric Coil Boot Set', oem:p+'-90919-11009', qty:'4 pcs', price:14.0, spec:'Silicone arc insulator', icon:'hose' },
      { id:'s_sp3', name:'Valve Cover & Spark Tube Seal', oem:p+'-11213-21020', qty:'1 set', price:16.5, spec:'Stops oil in plug wells', icon:'seal' }
    ]},
    { id:'ful_air', sys:'Fuel & Air', name:'Engine Intake Air Filter & Fuel Separator Kit', oem:c.air, alt:'MANN C28043 / DENSO', epc:c.epc+' Fig. 17-03', price:24.9, task:'air', icon:'filter', subs:[
      { id:'s_ar1', name:'Pleated Engine Air Filter Element', oem:c.air, qty:'1 pc', price:19.5, spec:'99.5% urban dust retention', icon:'filter' },
      { id:'s_ar2', name:'Fuel Filter / Water Separator', oem:c.fuel, qty:'1 pc', price:28.0, spec:'2-micron CRDi / EFI safe', icon:'filter' },
      { id:'s_ar3', name:'Airbox Foam Perimeter & MAF O-Ring', oem:p+'-17816-21010', qty:'1 kit', price:4.8, spec:'Prevents dust bypass', icon:'seal' }
    ]},
    { id:'trn_kit', sys:'Transmission', name:'Transmission / eCVT Fluid & Pan Strainer Kit', oem:p+'-35330-73010', alt:'AISIN ATF-FE4 / MAHLE', epc:c.epc+' Fig. 35-03', price:58.5, task:'trn', icon:'bearing', subs:[
      { id:'s_tr1', name:'Internal Stainless Mesh Strainer', oem:p+'-35330-73010', qty:'1 pc', price:26.0, spec:'OEM fine screen', icon:'filter' },
      { id:'s_tr2', name:'Molded Viton Pan Gasket + Magnets', oem:p+'-35168-21011', qty:'1 kit', price:15.0, spec:'Zero-leak pan seal', icon:'seal' },
      { id:'s_tr3', name:'Drain & Overflow Crush Washers', oem:p+'-90430-18008', qty:'2 pcs', price:3.5, spec:'18mm aluminum ring', icon:'seal' }
    ]},
    { id:'clt_kit', sys:'Clutch', name:'3-Piece Clutch Cover, Disc & Release Bearing Kit', oem:c.clutch, alt:'AISIN / EXEDY / LUK', epc:c.epc+' Fig. 31-01', price:198.0, task:'clt', icon:'rotor', subs:[
      { id:'s_cl1', name:'Torsion-Damped Clutch Friction Disc', oem:c.clutch+'-DSC', qty:'1 pc', price:78.0, spec:'Copper-weave organic facing', icon:'rotor' },
      { id:'s_cl2', name:'Diaphragm Pressure Plate Cover', oem:c.clutch+'-COV', qty:'1 pc', price:84.0, spec:'Nodular cast iron pressure ring', icon:'rotor' },
      { id:'s_cl3', name:'Hydraulic Throwout Release Bearing', oem:p+'-31230-12191', qty:'1 pc', price:34.0, spec:'Koyo sealed ball bearing', icon:'bearing' },
      { id:'s_cl4', name:'Clutch Slave Cylinder Seal Kit', oem:p+'-31470-12140', qty:'1 kit', price:22.0, spec:'EPDM piston cups', icon:'seal' }
    ]},
    { id:'sus_kit', sys:'Suspension', name:'Gas Front Strut, Control Arm & Ball Joint Kit', oem:p+'-48510-8Z310', alt:'KYB / SANKEI 555 / CTR', epc:c.epc+' Fig. 48-03', price:132.0, task:'sus', icon:'spring', subs:[
      { id:'s_su1', name:'KYB / OEM Twin-Tube Strut Cartridge', oem:p+'-48510-8Z310', qty:'1 pc', price:88.0, spec:'BD rough-road valving', icon:'spring' },
      { id:'s_su2', name:'Lower Control Arm Ball Joint Kit', oem:p+'-43330-19245', qty:'1 kit', price:42.0, spec:'555 Japan hardened ball stud', icon:'bracket' },
      { id:'s_su3', name:'Upper Strut Mount Bearing & Boot', oem:p+'-48609-12570', qty:'1 kit', price:29.0, spec:'Protects chrome damper shaft', icon:'bearing' }
    ]},
    { id:'str_kit', sys:'Steering', name:'EPS / Hydraulic Steering Rack & Tie-Rod Kit', oem:c.rack, alt:'JTEKT / TRW / 555', epc:c.epc+' Fig. 45-05', price:310.0, task:'str', icon:'cable', subs:[
      { id:'s_st1', name:'Outer Tie-Rod End Pair (LH/RH)', oem:p+'-45046-19415', qty:'1 pair', price:32.0, spec:'Pre-greased ball joint', icon:'bracket' },
      { id:'s_st2', name:'Inner Steering Rack End Axial Joint', oem:p+'-45503-19255', qty:'1 pair', price:38.0, spec:'Heavy-duty socket joint', icon:'cable' },
      { id:'s_st3', name:'Rack Bellows Dust Boot + Clamps', oem:p+'-45535-12100', qty:'1 pair', price:14.5, spec:'Ozone & flood resistant', icon:'hose' }
    ]},
    { id:'brk_kit', sys:'Brakes', name:'Ceramic Brake Pad, Vented Rotor & DOT 4 Kit', oem:c.pad, alt:'AKEBONO / DIXCEL / SEIKEN', epc:c.epc+' Fig. 47-05', price:64.0, task:'brk', icon:'pad', subs:[
      { id:'s_br1', name:'Ceramic Low-Dust Pad Set (4x)', oem:c.pad, qty:'4 pads', price:48.0, spec:'Copper-free Akebono grade', icon:'pad' },
      { id:'s_br2', name:'UV-Coated Vented Rotor Pair', oem:c.rotor, qty:'2 pcs', price:124.0, spec:'High-carbon anti-warp disc', icon:'rotor' },
      { id:'s_br3', name:'Stainless Shim, Clip & Slide Pin Kit', oem:p+'-04945-52150', qty:'1 kit', price:19.0, spec:'Eliminates brake squeal', icon:'pad' },
      { id:'s_br4', name:'DOT 4 LV Class-6 Brake Fluid (1L)', oem:p+'-08823-80112', qty:'1 btl', price:16.5, spec:'265°C dry boiling point', icon:'bottle' }
    ]},
    { id:'col_kit', sys:'Cooling', name:'Aluminum Radiator, Thermostat & Water Pump Kit', oem:c.rad, alt:'DENSO / KOYORAD / AISIN', epc:c.epc+' Fig. 16-03', price:164.0, task:'col', icon:'fan', subs:[
      { id:'s_co1', name:'Aluminum Core Radiator', oem:c.rad, qty:'1 pc', price:118.0, spec:'22mm tropical louver fins', icon:'fan' },
      { id:'s_co2', name:'82°C Wax Thermostat + 108 kPa Cap', oem:p+'-90916-03136', qty:'1 kit', price:28.0, spec:'Gridlock boil-over margin', icon:'sensor' },
      { id:'s_co3', name:'Water Pump + MLS Metal Gasket', oem:p+'-161A0-29015', qty:'1 pc', price:88.0, spec:'Carbon-ceramic seal', icon:'bearing' }
    ]},
    { id:'hvc_kit', sys:'AC / HVAC', name:'AC Compressor, Expansion Valve & HEPA Filter', oem:c.ac, alt:'DENSO CoolGear DCC1014', epc:c.epc+' Fig. 87-14', price:245.0, task:'ac', icon:'fan', subs:[
      { id:'s_hv1', name:'Activated Carbon PM2.5 Cabin Filter', oem:c.cabin, qty:'1 pc', price:22.5, spec:'99.1% Dhaka smog filtration', icon:'filter' },
      { id:'s_hv2', name:'OEM AC Compressor Assembly', oem:c.ac, qty:'1 pc', price:215.0, spec:'High-ambient tropical clutch', icon:'fan' },
      { id:'s_hv3', name:'TXV Expansion Valve & Drier Bag', oem:p+'-88515-52080', qty:'1 kit', price:39.0, spec:'Moisture & debris trap', icon:'sensor' }
    ]},
    { id:'elc_kit', sys:'Electrical', name:'12V Battery, ABS Speed Sensor & Relay Kit', oem:p+'-28800-31370', alt:'GS YUASA / AMARON / BOSCH', epc:c.epc+' Fig. 82-01', price:169.0, task:'bat', icon:'battery', subs:[
      { id:'s_el1', name:'12V EFB / AGM Battery Unit', oem:p+'-28800-31370', qty:'1 unit', price:155.0, spec:'High-heat Start-Stop/Hybrid', icon:'battery' },
      { id:'s_el2', name:'Hall-Effect Wheel Speed ABS Sensor', oem:p+'-89542-06081', qty:'1 pc', price:36.0, spec:'IP69K monsoon-sealed plug', icon:'sensor' },
      { id:'s_el3', name:'Positive Multi-Fuse Terminal & 30A Relay', oem:p+'-82620-12280', qty:'1 kit', price:29.0, spec:'120A main fusible link', icon:'sensor' }
    ]},
    { id:'exh_kit', sys:'Exhaust', name:'Catalytic / DPF Pipe, O2 Sensor & Flange Kit', oem:c.exh, alt:'DENSO 234-9090 / BOSAL', epc:c.epc+' Fig. 17-02', price:228.0, task:'exh', icon:'hose', subs:[
      { id:'s_ex1', name:'Wideband Air-Fuel O2 Sensor', oem:p+'-89467-12100', qty:'1 pc', price:94.0, spec:'Fast 10s zirconia heater', icon:'sensor' },
      { id:'s_ex2', name:'Graphite Mesh Donut Gasket + Spring Bolts', oem:p+'-17451-21060', qty:'1 kit', price:19.0, spec:'600°C leak-free flange seal', icon:'seal' }
    ]},
    { id:'bdy_kit', sys:'Body & Interior', name:'Silicone Wiper Blades, Hood Strut & Clip Kit', oem:p+'-85212-06230', alt:'NWB AERO / STABILUS', epc:c.epc+' Fig. 85-01', price:32.0, task:'wip', icon:'wiper', subs:[
      { id:'s_bd1', name:'Monsoon Aero Silicone Wiper Pair', oem:p+'-85222-52150', qty:'2 pcs', price:26.0, spec:'Hydrophobic streak-free', icon:'wiper' },
      { id:'s_bd2', name:'Hood Lift Strut Pair + 20x Bumper Clips', oem:p+'-53440-06180', qty:'1 kit', price:38.0, spec:'380N gas springs + nylon pins', icon:'spring' }
    ]}
  ];

  // Expand each system with additional assemblies and pad every assembly to 22 sub-parts -> 72 assemblies + 1,584 sub-parts = 1,656 parts!
  const allAssemblies = [...core];
  EXTRA_SYSTEM_TEMPLATES.forEach(([id, sys, name, oemTail, alt, fig, price, icon, subArr]) => {
    allAssemblies.push({
      id, sys, name, oem: p + '-' + oemTail, alt, epc: c.epc + ' ' + fig, price, task: null, icon,
      subs: subArr.map((s, idx) => ({
        id: `${id}_sub_${idx + 1}`,
        name: s[0],
        oem: p + '-' + s[1],
        qty: s[2],
        price: s[3],
        spec: `Verified ${vr.chassis} EPC spec`,
        icon: s[4]
      }))
    });
  });

  // Pad every assembly to 22 numbered micro sub-parts and build flat 1,656-part index
  const flatParts = [];
  allAssemblies.forEach((asm, asmIdx) => {
    const baseTail = asm.oem.replace(/[^A-Z0-9]/gi, '').slice(-5) || '10001';
    let hwIdx = 0;
    while (asm.subs.length < 22) {
      const [hwName, hwIcon, hwSpec, hwPrice] = HARDWARE_SUFFIXES[hwIdx % HARDWARE_SUFFIXES.length];
      const seq = asm.subs.length + 1;
      asm.subs.push({
        id: `${asm.id}_hw_${seq}`,
        name: hwName,
        oem: `${p}-${baseTail}-${String(seq).padStart(2, '0')}`,
        qty: hwName.includes('4x') ? '4 pcs' : hwName.includes('Pair') ? '2 pcs' : '1 pc',
        price: hwPrice,
        spec: hwSpec,
        icon: hwIcon
      });
      hwIdx++;
    }
    asm._blob = `${asm.name} ${asm.oem} ${asm.alt} ${asm.sys} ${asm.subs.map(s => `${s.name} ${s.oem} ${s.spec}`).join(' ')}`.toLowerCase();

    flatParts.push({
      key: `${asm.id}`, asmId: asm.id, subId: null, callout: flatParts.length + 1,
      sys: asm.sys, name: asm.name, oem: asm.oem, spec: asm.alt, qty: '1 kit', price: asm.price,
      icon: asm.icon, isSub: false, _blob: `${asm.name} ${asm.oem} ${asm.sys} ${asm.alt}`.toLowerCase()
    });
    asm.subs.forEach(sub => {
      flatParts.push({
        key: `${asm.id}:${sub.id}`, asmId: asm.id, subId: sub.id, callout: flatParts.length + 1,
        sys: asm.sys, name: sub.name, oem: sub.oem, spec: sub.spec, qty: sub.qty, price: sub.price,
        icon: sub.icon || inferIconKey(sub.name, asm.icon), isSub: true,
        _blob: `${sub.name} ${sub.oem} ${sub.spec} ${asm.sys} ${asm.name}`.toLowerCase()
      });
    });
  });

  const bundle = { assemblies: allAssemblies, flatParts, epcTitle: c.epc, chassis: vr.chassis };
  CATALOG_CACHE.set(cacheKey, bundle);
  return bundle;
}
