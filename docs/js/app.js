/**
 * EquipTrack - Enterprise Heavy Equipment Fleet & Logistics
 * State Management, Interactive Filter Engine & Contract Operations
 */

// --- Clean SVG Icons (Lucide-style Line Vectors) ---
const ICONS = {
  search: `<svg class="icon" viewBox="0 0 24 24"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>`,
  calendar: `<svg class="icon" viewBox="0 0 24 24"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>`,
  mapPin: `<svg class="icon" viewBox="0 0 24 24"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/><circle cx="12" cy="10" r="3"/></svg>`,
  truck: `<svg class="icon" viewBox="0 0 24 24"><rect x="1" y="3" width="15" height="13"/><polygon points="16 8 20 8 23 11 23 16 16 16 16 8"/><circle cx="5.5" cy="18.5" r="2.5"/><circle cx="18.5" cy="18.5" r="2.5"/></svg>`,
  shield: `<svg class="icon" viewBox="0 0 24 24"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>`,
  fileText: `<svg class="icon" viewBox="0 0 24 24"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/></svg>`,
  check: `<svg class="icon" viewBox="0 0 24 24"><polyline points="20 6 9 17 4 12"/></svg>`,
  x: `<svg class="icon" viewBox="0 0 24 24"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>`,
  clock: `<svg class="icon" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>`,
  user: `<svg class="icon" viewBox="0 0 24 24"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>`,
  refresh: `<svg class="icon icon-sm" viewBox="0 0 24 24"><polyline points="23 4 23 10 17 10"/><path d="M20.49 15a9 9 0 1 1-2.12-9.36L23 10"/></svg>`,
  plus: `<svg class="icon icon-sm" viewBox="0 0 24 24"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>`,
  sliders: `<svg class="icon icon-sm" viewBox="0 0 24 24"><line x1="4" y1="21" x2="4" y2="14"/><line x1="4" y1="10" x2="4" y2="3"/><line x1="12" y1="21" x2="12" y2="12"/><line x1="12" y1="8" x2="12" y2="3"/><line x1="20" y1="21" x2="20" y2="16"/><line x1="20" y1="12" x2="20" y2="3"/><line x1="1" y1="14" x2="7" y2="14"/><line x1="9" y1="8" x2="15" y2="8"/><line x1="17" y1="16" x2="23" y2="16"/></svg>`
};

// --- Enterprise Dataset ---
const DEFAULT_CATEGORIES = [
  { id: "CAT-HM", name: "Heavy Machinery" },
  { id: "CAT-MH", name: "Material Handling" },
  { id: "CAT-AL", name: "Aerial Access" },
  { id: "CAT-CE", name: "Concrete & Pumping" },
  { id: "CAT-CP", name: "Compaction Equipment" },
  { id: "CAT-PT", name: "Power Generation" }
];

const DEFAULT_LOCATIONS = [
  { id: "LOC-NY", name: "Depot North - East Hub", city: "New York, NY", address: "123 Industrial Boulevard", phone: "+1 (555) 100-1000", hours: "Mon-Sat 06:00 - 18:00 EST" },
  { id: "LOC-LA", name: "Depot West - Pacific Logistics", city: "Los Angeles, CA", address: "456 Logistics Way", phone: "+1 (555) 200-2000", hours: "Mon-Sat 06:00 - 19:00 PST" },
  { id: "LOC-CH", name: "Depot Central - Midwest Yard", city: "Chicago, IL", address: "789 Commercial Parkway", phone: "+1 (555) 300-3000", hours: "Mon-Fri 07:00 - 17:00 CST" }
];

const DEFAULT_EQUIPMENT = [
  {
    id: "EQ000001",
    name: "Caterpillar 320 Hydraulic Excavator",
    code: "CAT-320D3",
    category: "CAT-HM",
    categoryName: "Heavy Machinery",
    location: "LOC-NY",
    locationName: "Depot North (East Hub)",
    dailyRate: 38250,
    weeklyRate: 238000,
    securityDeposit: 170000,
    status: "AVAILABLE",
    condition: "CERTIFIED",
    manufacturer: "Caterpillar Inc.",
    model: "320 D3 Series",
    year: 2022,
    operatingWeight: "22,500 kg",
    horsepower: "174 HP @ 2000 rpm",
    digDepth: "6.72 meters",
    fuelType: "Ultra-low Sulfur Diesel",
    description: "Standard industrial crawler excavator fitted with Grade Assist technology and standard 1.19 m³ bucket capacity.",
    featured: true
  },
  {
    id: "EQ000002",
    name: "Caterpillar D6 Track-Type Tractor",
    code: "CAT-D6T",
    category: "CAT-HM",
    categoryName: "Heavy Machinery",
    location: "LOC-NY",
    locationName: "Depot North (East Hub)",
    dailyRate: 46750,
    weeklyRate: 297500,
    securityDeposit: 212500,
    status: "AVAILABLE",
    condition: "CERTIFIED",
    manufacturer: "Caterpillar Inc.",
    model: "D6 T-Series XL",
    year: 2021,
    operatingWeight: "19,800 kg",
    horsepower: "215 HP Net",
    digDepth: "Blade Capacity 5.6 m³",
    fuelType: "Heavy Diesel",
    description: "Heavy production crawler dozer equipped with semi-universal blade and differential steering for grading and clearing.",
    featured: true
  },
  {
    id: "EQ000003",
    name: "JCB 3DX Super Backhoe Loader",
    code: "JCB-3DXS",
    category: "CAT-HM",
    categoryName: "Heavy Machinery",
    location: "LOC-LA",
    locationName: "Depot West (Pacific)",
    dailyRate: 29750,
    weeklyRate: 191250,
    securityDeposit: 127500,
    status: "RENTED",
    condition: "IN SERVICE",
    manufacturer: "JCB Corporation",
    model: "3DX Super EcoMax",
    year: 2023,
    operatingWeight: "7,600 kg",
    horsepower: "76 HP Turbo",
    digDepth: "4.77 meters",
    fuelType: "Diesel",
    description: "Multi-purpose wheeled utility backhoe with heavy-duty front shovel and telescoping dipper arm.",
    featured: true
  },
  {
    id: "EQ000004",
    name: "Bobcat S590 Skid-Steer Loader",
    code: "BOB-S590",
    category: "CAT-MH",
    categoryName: "Material Handling",
    location: "LOC-LA",
    locationName: "Depot West (Pacific)",
    dailyRate: 18500,
    weeklyRate: 118000,
    securityDeposit: 75000,
    status: "AVAILABLE",
    condition: "CERTIFIED",
    manufacturer: "Bobcat Doosan",
    model: "S590 T4 Vertical Lift",
    year: 2023,
    operatingWeight: "3,127 kg",
    horsepower: "68 HP Tier 4",
    digDepth: "Hinge Pin Height 3.02 m",
    fuelType: "Diesel",
    description: "Vertical-path compact skid loader engineered for pallet unloading, tight utility access, and civil work.",
    featured: false
  },
  {
    id: "EQ000005",
    name: "Toyota 8FDU30 Industrial Forklift",
    code: "TOY-8FDU30",
    category: "CAT-MH",
    categoryName: "Material Handling",
    location: "LOC-CH",
    locationName: "Depot Central (Midwest)",
    dailyRate: 14200,
    weeklyRate: 89000,
    securityDeposit: 60000,
    status: "AVAILABLE",
    condition: "CERTIFIED",
    manufacturer: "Toyota Material Handling",
    model: "8FDU30 Pneumatic",
    year: 2022,
    operatingWeight: "4,250 kg",
    horsepower: "55 HP Industrial",
    digDepth: "Mast Lift 4.80 m (3-Stage)",
    fuelType: "Diesel / Dual-Fuel",
    description: "Heavy rough-terrain forklift with pneumatic tires and integral side-shifter for logistics yards and steel yards.",
    featured: false
  },
  {
    id: "EQ000006",
    name: "Genie S-85 Telescopic Boom Lift",
    code: "GEN-S85",
    category: "CAT-AL",
    categoryName: "Aerial Access",
    location: "LOC-NY",
    locationName: "Depot North (East Hub)",
    dailyRate: 32000,
    weeklyRate: 205000,
    securityDeposit: 140000,
    status: "AVAILABLE",
    condition: "CERTIFIED",
    manufacturer: "Terex Genie",
    model: "S-85 XC 4WD",
    year: 2022,
    operatingWeight: "17,900 kg",
    horsepower: "74 HP Diesel",
    digDepth: "Working Height 27.9 m",
    fuelType: "Diesel",
    description: "High-capacity rough-terrain telescoping personnel lift with 360-degree continuous turntable rotation.",
    featured: true
  },
  {
    id: "EQ000007",
    name: "Bomag BW 211 D-5 Soil Compactor",
    code: "BOM-BW211",
    category: "CAT-CP",
    categoryName: "Compaction Equipment",
    location: "LOC-LA",
    locationName: "Depot West (Pacific)",
    dailyRate: 26500,
    weeklyRate: 168000,
    securityDeposit: 110000,
    status: "MAINTENANCE",
    condition: "SCHEDULED SERVICE",
    manufacturer: "Bomag Fayat Group",
    model: "BW 211 D-5 Tier 4f",
    year: 2020,
    operatingWeight: "11,200 kg",
    horsepower: "128 HP Deutz",
    digDepth: "Drum Width 2,130 mm",
    fuelType: "Diesel",
    description: "Single-drum vibratory roller for deep soil compaction, embankment foundation, and highway sub-base.",
    featured: false
  },
  {
    id: "EQ000008",
    name: "Cummins C125D5 Acoustic Generator",
    code: "CUM-C125",
    category: "CAT-PT",
    categoryName: "Power Generation",
    location: "LOC-CH",
    locationName: "Depot Central (Midwest)",
    dailyRate: 12500,
    weeklyRate: 79000,
    securityDeposit: 50000,
    status: "AVAILABLE",
    condition: "CERTIFIED",
    manufacturer: "Cummins Power Systems",
    model: "C125D5 Enclosed",
    year: 2023,
    operatingWeight: "1,980 kg",
    horsepower: "140 HP 6BTAA",
    digDepth: "Prime 125 kVA / 100 kW",
    fuelType: "Diesel (24h internal tank)",
    description: "Weather-protective sound-attenuated diesel generation set with digital auto-start controller.",
    featured: false
  }
];

const DEFAULT_BOOKINGS = [
  {
    id: "EQT-2026-9811",
    equipmentId: "EQ000001",
    equipmentName: "Caterpillar 320 Hydraulic Excavator",
    customerName: "Apex Infrastructure Contractors",
    customerEmail: "customer@test.com",
    startDate: "2026-10-25",
    endDate: "2026-10-30",
    days: 6,
    subtotal: 229500,
    tax: 11475,
    total: 240975,
    location: "Depot North - East Hub",
    status: "APPROVED",
    pickupType: "Commercial Hauler Dispatch",
    operatorRequested: true,
    createdDate: "2026-10-18"
  },
  {
    id: "EQT-2026-9812",
    equipmentId: "EQ000003",
    equipmentName: "JCB 3DX Super Backhoe Loader",
    customerName: "Midland Civil Engineering LLC",
    customerEmail: "logistics@midlandcivil.com",
    startDate: "2026-10-15",
    endDate: "2026-11-05",
    days: 22,
    subtotal: 654500,
    tax: 32725,
    total: 687225,
    location: "Depot West - Pacific Logistics",
    status: "ACTIVE",
    pickupType: "Direct Depot Pickup",
    operatorRequested: false,
    createdDate: "2026-10-12"
  },
  {
    id: "EQT-2026-9813",
    equipmentId: "EQ000006",
    equipmentName: "Genie S-85 Telescopic Boom Lift",
    customerName: "Apex Infrastructure Contractors",
    customerEmail: "customer@test.com",
    startDate: "2026-11-01",
    endDate: "2026-11-04",
    days: 4,
    subtotal: 128000,
    tax: 6400,
    total: 134400,
    location: "Depot North - East Hub",
    status: "PENDING",
    pickupType: "Direct Depot Pickup",
    operatorRequested: false,
    createdDate: "2026-10-19"
  }
];

class EquipTrackApp {
  constructor() {
    this.initDatabase();
    this.currentRole = localStorage.getItem("et_active_role") || "customer";
    this.currentTab = "home";
    this.selectedEquipment = null;
    this.filters = {
      search: "",
      category: "ALL",
      status: "ALL",
      location: "ALL",
      sortBy: "FEATURED"
    };
    this.bookingCalculation = {
      days: 0,
      subtotal: 0,
      tax: 0,
      total: 0
    };

    this.bindEvents();
    this.render();
  }

  initDatabase() {
    if (!localStorage.getItem("et_equipment")) {
      localStorage.setItem("et_equipment", JSON.stringify(DEFAULT_EQUIPMENT));
    }
    if (!localStorage.getItem("et_bookings")) {
      localStorage.setItem("et_bookings", JSON.stringify(DEFAULT_BOOKINGS));
    }
  }

  getEquipmentList() {
    return JSON.parse(localStorage.getItem("et_equipment")) || [];
  }

  saveEquipmentList(list) {
    localStorage.setItem("et_equipment", JSON.stringify(list));
  }

  getBookingsList() {
    return JSON.parse(localStorage.getItem("et_bookings")) || [];
  }

  saveBookingsList(list) {
    localStorage.setItem("et_bookings", JSON.stringify(list));
  }

  resetAllData() {
    localStorage.setItem("et_equipment", JSON.stringify(DEFAULT_EQUIPMENT));
    localStorage.setItem("et_bookings", JSON.stringify(DEFAULT_BOOKINGS));
    this.showToast("Database restored to default enterprise fleet records.", "success");
    this.render();
  }

  setRole(newRole) {
    this.currentRole = newRole;
    localStorage.setItem("et_active_role", newRole);
    
    if (newRole === "guest" && (this.currentTab === "bookings" || this.currentTab === "admin")) {
      this.currentTab = "browse";
    } else if (newRole === "customer" && this.currentTab === "admin") {
      this.currentTab = "browse";
    }

    const roleName = newRole === "admin" ? "Fleet Operations Director" : (newRole === "customer" ? "Contractor Account" : "Public View");
    this.showToast(`Switched active context to: ${roleName}`, "info");
    this.render();
  }

  switchTab(tabId) {
    this.currentTab = tabId;
    window.scrollTo({ top: 0, behavior: "smooth" });
    this.render();
  }

  formatCurrency(val) {
    return "₹" + Number(val).toLocaleString("en-IN");
  }

  getFilteredEquipment() {
    let list = this.getEquipmentList();

    if (this.filters.search) {
      const q = this.filters.search.toLowerCase();
      list = list.filter(item => 
        item.name.toLowerCase().includes(q) ||
        item.manufacturer.toLowerCase().includes(q) ||
        item.model.toLowerCase().includes(q) ||
        item.code.toLowerCase().includes(q) ||
        item.description.toLowerCase().includes(q)
      );
    }

    if (this.filters.category !== "ALL") {
      list = list.filter(item => item.category === this.filters.category);
    }

    if (this.filters.status !== "ALL") {
      list = list.filter(item => item.status === this.filters.status);
    }

    if (this.filters.location !== "ALL") {
      list = list.filter(item => item.location === this.filters.location);
    }

    if (this.filters.sortBy === "PRICE_ASC") {
      list.sort((a, b) => a.dailyRate - b.dailyRate);
    } else if (this.filters.sortBy === "PRICE_DESC") {
      list.sort((a, b) => b.dailyRate - a.dailyRate);
    } else if (this.filters.sortBy === "NAME_ASC") {
      list.sort((a, b) => a.name.localeCompare(b.name));
    } else {
      list.sort((a, b) => (b.featured ? 1 : 0) - (a.featured ? 1 : 0));
    }

    return list;
  }

  calculatePricing(equipment, startDateStr, endDateStr) {
    if (!startDateStr || !endDateStr) {
      return { days: 0, subtotal: 0, tax: 0, total: 0 };
    }
    const start = new Date(startDateStr);
    const end = new Date(endDateStr);
    if (end < start) {
      return { days: 0, subtotal: 0, tax: 0, total: 0 };
    }
    const diffTime = Math.abs(end - start);
    const days = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
    const subtotal = days * equipment.dailyRate;
    const tax = Math.round(subtotal * 0.05);
    const total = subtotal + tax;
    return { days, subtotal, tax, total };
  }

  openEquipmentModal(eqId) {
    const eq = this.getEquipmentList().find(e => e.id === eqId);
    if (!eq) return;
    this.selectedEquipment = eq;

    const modal = document.getElementById("equipmentDetailsModal");
    const today = new Date().toISOString().split("T")[0];
    const nextWeek = new Date(Date.now() + 5 * 24 * 60 * 60 * 1000).toISOString().split("T")[0];

    document.getElementById("modalEqName").textContent = eq.name;
    document.getElementById("modalEqCode").textContent = eq.code;
    document.getElementById("modalEqCategory").textContent = eq.categoryName;
    document.getElementById("modalEqLocation").textContent = eq.locationName;
    document.getElementById("modalEqDesc").textContent = eq.description;
    document.getElementById("modalEqRate").textContent = this.formatCurrency(eq.dailyRate);
    document.getElementById("modalEqWeekly").textContent = this.formatCurrency(eq.weeklyRate);
    document.getElementById("modalEqDeposit").textContent = this.formatCurrency(eq.securityDeposit);
    document.getElementById("modalEqWeight").textContent = eq.operatingWeight || "N/A";
    document.getElementById("modalEqHP").textContent = eq.horsepower || "N/A";
    document.getElementById("modalEqDepth").textContent = eq.digDepth || "N/A";
    document.getElementById("modalEqFuel").textContent = eq.fuelType || "Standard Diesel";
    document.getElementById("modalEqYear").textContent = eq.year || "2022";
    document.getElementById("modalEqMfg").textContent = eq.manufacturer;

    const statusBadge = document.getElementById("modalEqStatus");
    statusBadge.className = `badge-status ${eq.status.toLowerCase()}`;
    statusBadge.innerHTML = `<span class="badge-dot"></span> ${eq.status}`;

    const startInput = document.getElementById("bookingStartDate");
    const endInput = document.getElementById("bookingEndDate");
    startInput.min = today;
    startInput.value = today;
    endInput.min = today;
    endInput.value = nextWeek;

    this.updateModalCalculation();
    modal.classList.add("active");
  }

  closeEquipmentModal() {
    document.getElementById("equipmentDetailsModal").classList.remove("active");
  }

  updateModalCalculation() {
    if (!this.selectedEquipment) return;
    const startVal = document.getElementById("bookingStartDate").value;
    const endVal = document.getElementById("bookingEndDate").value;
    const calc = this.calculatePricing(this.selectedEquipment, startVal, endVal);
    this.bookingCalculation = calc;

    document.getElementById("calcDays").textContent = `${calc.days} Day${calc.days === 1 ? '' : 's'}`;
    document.getElementById("calcSubtotal").textContent = this.formatCurrency(calc.subtotal);
    document.getElementById("calcTax").textContent = this.formatCurrency(calc.tax);
    document.getElementById("calcTotal").textContent = this.formatCurrency(calc.total);

    const bookBtn = document.getElementById("btnProceedCheckout");
    if (calc.days > 0 && this.selectedEquipment.status === "AVAILABLE") {
      bookBtn.disabled = false;
      bookBtn.textContent = `Reserve Fleet • ${this.formatCurrency(calc.total)}`;
    } else if (this.selectedEquipment.status !== "AVAILABLE") {
      bookBtn.disabled = true;
      bookBtn.textContent = `Unit is currently ${this.selectedEquipment.status}`;
    } else {
      bookBtn.disabled = true;
      bookBtn.textContent = "Specify Valid Lease Dates";
    }
  }

  proceedToCheckout() {
    if (this.currentRole === "guest") {
      this.closeEquipmentModal();
      this.showToast("Please switch to Contractor Account to reserve equipment.", "danger");
      return;
    }
    this.closeEquipmentModal();
    this.openCheckoutModal();
  }

  openCheckoutModal() {
    const modal = document.getElementById("checkoutModal");
    const eq = this.selectedEquipment;
    const calc = this.bookingCalculation;
    if (!eq) return;

    document.getElementById("checkoutMachineName").textContent = eq.name;
    document.getElementById("checkoutDuration").textContent = `${calc.days} Days (${document.getElementById("bookingStartDate").value} through ${document.getElementById("bookingEndDate").value})`;
    document.getElementById("checkoutTotalPayable").textContent = this.formatCurrency(calc.total);

    modal.classList.add("active");
  }

  closeCheckoutModal() {
    document.getElementById("checkoutModal").classList.remove("active");
  }

  submitBookingOrder() {
    const eq = this.selectedEquipment;
    const calc = this.bookingCalculation;
    const startDate = document.getElementById("bookingStartDate").value;
    const endDate = document.getElementById("bookingEndDate").value;
    const notes = document.getElementById("checkoutProjectNotes").value;
    const delivery = document.getElementById("checkoutDeliveryOption").value;
    const operator = document.getElementById("checkoutOperatorCheck").checked;

    const bookingId = "EQT-2026-" + Math.floor(1000 + Math.random() * 9000);

    const newBooking = {
      id: bookingId,
      equipmentId: eq.id,
      equipmentName: eq.name,
      customerName: "Apex Infrastructure Contractors",
      customerEmail: "customer@test.com",
      startDate: startDate,
      endDate: endDate,
      days: calc.days,
      subtotal: calc.subtotal,
      tax: calc.tax,
      total: calc.total,
      location: eq.locationName,
      status: "APPROVED",
      pickupType: delivery,
      operatorRequested: operator,
      notes: notes,
      createdDate: new Date().toISOString().split("T")[0]
    };

    const bookings = this.getBookingsList();
    bookings.unshift(newBooking);
    this.saveBookingsList(bookings);

    this.closeCheckoutModal();
    this.showToast(`Contract ${bookingId} successfully confirmed and scheduled.`, "success");
    this.switchTab("bookings");
  }

  approveBooking(bookingId) {
    const list = this.getBookingsList();
    const b = list.find(x => x.id === bookingId);
    if (b) {
      b.status = "APPROVED";
      this.saveBookingsList(list);
      this.showToast(`Rental contract ${bookingId} approved.`, "success");
      this.render();
    }
  }

  rejectBooking(bookingId) {
    const list = this.getBookingsList();
    const b = list.find(x => x.id === bookingId);
    if (b) {
      b.status = "CANCELLED";
      this.saveBookingsList(list);
      this.showToast(`Rental contract ${bookingId} rejected.`, "danger");
      this.render();
    }
  }

  toggleEquipmentStatus(eqId, newStatus) {
    const list = this.getEquipmentList();
    const eq = list.find(x => x.id === eqId);
    if (eq) {
      eq.status = newStatus;
      this.saveEquipmentList(list);
      this.showToast(`Fleet unit ${eq.code} status changed to ${newStatus}.`, "info");
      this.render();
    }
  }

  openAddEquipmentModal() {
    document.getElementById("addEquipmentModal").classList.add("active");
  }

  closeAddEquipmentModal() {
    document.getElementById("addEquipmentModal").classList.remove("active");
  }

  submitNewEquipment(e) {
    e.preventDefault();
    const name = document.getElementById("newEqName").value.trim();
    const category = document.getElementById("newEqCategory").value;
    const location = document.getElementById("newEqLocation").value;
    const rate = Number(document.getElementById("newEqRate").value) || 25000;
    const mfg = document.getElementById("newEqMfg").value.trim() || "Komatsu";
    const model = document.getElementById("newEqModel").value.trim() || "Standard Series";
    const desc = document.getElementById("newEqDesc").value.trim() || "Heavy machinery unit for civil infrastructure.";

    const categoryObj = DEFAULT_CATEGORIES.find(c => c.id === category);
    const locationObj = DEFAULT_LOCATIONS.find(l => l.id === location);

    const newEq = {
      id: "EQ0000" + Math.floor(10 + Math.random() * 90),
      name: name,
      code: "KOM-" + Math.floor(100 + Math.random() * 900),
      category: category,
      categoryName: categoryObj ? categoryObj.name : "Heavy Machinery",
      location: location,
      locationName: locationObj ? locationObj.name : "Depot North",
      dailyRate: rate,
      weeklyRate: Math.round(rate * 6),
      securityDeposit: Math.round(rate * 4),
      status: "AVAILABLE",
      condition: "CERTIFIED",
      manufacturer: mfg,
      model: model,
      year: 2023,
      operatingWeight: "16,500 kg",
      horsepower: "150 HP",
      digDepth: "5.8 meters",
      fuelType: "Ultra-low Sulfur Diesel",
      description: desc,
      featured: true
    };

    const list = this.getEquipmentList();
    list.unshift(newEq);
    this.saveEquipmentList(list);

    this.closeAddEquipmentModal();
    this.showToast(`Fleet unit ${name} enrolled in inventory.`, "success");
    this.render();
  }

  cancelBooking(bookingId) {
    if (confirm(`Confirm cancellation of rental contract ${bookingId}?`)) {
      const list = this.getBookingsList();
      const b = list.find(x => x.id === bookingId);
      if (b) {
        b.status = "CANCELLED";
        this.saveBookingsList(list);
        this.showToast(`Contract ${bookingId} has been cancelled.`, "info");
        this.render();
      }
    }
  }

  downloadInvoice(bookingId) {
    this.showToast(`Commercial Contract Invoice #${bookingId}.pdf downloaded.`, "success");
  }

  showToast(message, type = "info") {
    const shelf = document.getElementById("toastShelf");
    if (!shelf) return;

    const toast = document.createElement("div");
    toast.className = `toast-message ${type}`;
    toast.innerHTML = `<span>${message}</span>`;
    shelf.appendChild(toast);

    setTimeout(() => {
      toast.style.opacity = "0";
      toast.style.transform = "translateY(6px)";
      toast.style.transition = "all 0.2s";
      setTimeout(() => toast.remove(), 200);
    }, 3200);
  }

  render() {
    this.renderRoleBar();
    this.renderNavbar();
    this.renderActiveTab();
  }

  renderRoleBar() {
    document.querySelectorAll(".role-pill").forEach(btn => {
      btn.classList.toggle("active", btn.dataset.role === this.currentRole);
    });

    const activeLabel = document.getElementById("activePersonaLabel");
    if (activeLabel) {
      if (this.currentRole === "admin") {
        activeLabel.textContent = "Fleet Operations Director (Admin)";
      } else if (this.currentRole === "customer") {
        activeLabel.textContent = "Apex Infrastructure Contractors (Commercial Account)";
      } else {
        activeLabel.textContent = "Public Catalog Viewer";
      }
    }
  }

  renderNavbar() {
    document.querySelectorAll(".tab-btn").forEach(tab => {
      tab.classList.toggle("active", tab.dataset.tab === this.currentTab);
    });

    const myBookings = this.getBookingsList().filter(b => b.customerEmail === "customer@test.com" && b.status !== "CANCELLED");
    const badge = document.getElementById("myBookingsBadge");
    if (badge) {
      badge.textContent = myBookings.length;
    }

    const customerLinks = document.getElementById("customerNavLinks");
    const adminLinks = document.getElementById("adminNavLinks");
    if (customerLinks) {
      customerLinks.style.display = this.currentRole === "customer" ? "inline-flex" : "none";
    }
    if (adminLinks) {
      adminLinks.style.display = this.currentRole === "admin" ? "inline-flex" : "none";
    }
  }

  renderActiveTab() {
    document.querySelectorAll(".tab-view").forEach(view => {
      view.style.display = "none";
    });

    const activeView = document.getElementById(`tab-${this.currentTab}`);
    if (activeView) {
      activeView.style.display = "block";
    }

    if (this.currentTab === "home") {
      this.renderHomeTab();
    } else if (this.currentTab === "browse") {
      this.renderBrowseTab();
    } else if (this.currentTab === "bookings") {
      this.renderBookingsTab();
    } else if (this.currentTab === "admin") {
      this.renderAdminTab();
    }
  }

  renderHomeTab() {
    const featuredSlot = document.getElementById("homeFeaturedGrid");
    if (!featuredSlot) return;

    const featured = this.getEquipmentList().filter(e => e.featured).slice(0, 3);
    featuredSlot.innerHTML = featured.map(eq => this.createEquipmentCardHTML(eq)).join("");
  }

  renderBrowseTab() {
    const grid = document.getElementById("browseEquipmentGrid");
    const empty = document.getElementById("browseEmptyState");
    const countBadge = document.getElementById("catalogResultsCount");
    if (!grid) return;

    const filtered = this.getFilteredEquipment();
    if (countBadge) {
      countBadge.textContent = `${filtered.length} Machines Listed`;
    }

    if (filtered.length === 0) {
      grid.style.display = "none";
      if (empty) empty.style.display = "block";
    } else {
      grid.style.display = "grid";
      if (empty) empty.style.display = "none";
      grid.innerHTML = filtered.map(eq => this.createEquipmentCardHTML(eq)).join("");
    }
  }

  createEquipmentCardHTML(eq) {
    const isAvail = eq.status === "AVAILABLE";

    return `
      <article class="fleet-card">
        <header class="fleet-card-header">
          <div>
            <span class="fleet-card-code">${eq.code}</span>
            <h3 class="fleet-card-title">${eq.name}</h3>
            <span class="fleet-card-mfg">${eq.manufacturer}</span>
          </div>
          <span class="badge-status ${eq.status.toLowerCase()}">
            <span class="badge-dot"></span> ${eq.status}
          </span>
        </header>

        <div class="fleet-card-body">
          <div class="fleet-location-chip">
            ${ICONS.mapPin}
            <span>${eq.locationName}</span>
          </div>

          <p style="font-size: 0.85rem; color: var(--slate-600); line-height: 1.5; margin-bottom: 0.5rem; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">
            ${eq.description}
          </p>

          <table class="fleet-specs-table">
            <tbody>
              <tr>
                <td class="spec-label">Operating Weight</td>
                <td class="spec-value">${eq.operatingWeight || 'Standard'}</td>
              </tr>
              <tr>
                <td class="spec-label">Gross Power</td>
                <td class="spec-value">${eq.horsepower || 'Heavy Class'}</td>
              </tr>
              <tr>
                <td class="spec-label">Dig Depth / Lift</td>
                <td class="spec-value">${eq.digDepth || 'Standard'}</td>
              </tr>
            </tbody>
          </table>

          <footer class="fleet-card-footer">
            <div class="fleet-rate-group">
              <span class="rate-label">Day Lease</span>
              <span class="rate-number">${this.formatCurrency(eq.dailyRate)} <span class="rate-unit">/day</span></span>
            </div>
            <button class="btn ${isAvail ? 'btn-accent' : 'btn-outline'}" 
                    onclick="app.openEquipmentModal('${eq.id}')">
              ${isAvail ? 'Reserve Fleet' : 'View Specs'}
            </button>
          </footer>
        </div>
      </article>
    `;
  }

  renderBookingsTab() {
    const tbody = document.getElementById("myBookingsTableBody");
    const empty = document.getElementById("myBookingsEmpty");
    const countLabel = document.getElementById("myBookingsActiveCount");
    const totalSpentLabel = document.getElementById("myBookingsTotalSpent");
    if (!tbody) return;

    const list = this.getBookingsList().filter(b => b.customerEmail === "customer@test.com");
    const activeCount = list.filter(b => b.status === "APPROVED" || b.status === "ACTIVE").length;
    const totalSpent = list.filter(b => b.status !== "CANCELLED").reduce((acc, b) => acc + b.total, 0);

    if (countLabel) countLabel.textContent = activeCount;
    if (totalSpentLabel) totalSpentLabel.textContent = this.formatCurrency(totalSpent);

    if (list.length === 0) {
      tbody.innerHTML = "";
      if (empty) empty.style.display = "block";
      return;
    }

    if (empty) empty.style.display = "none";
    tbody.innerHTML = list.map(b => {
      let statusClass = "available";
      if (b.status === "PENDING") statusClass = "rented";
      if (b.status === "CANCELLED") statusClass = "cancelled";

      return `
        <tr>
          <td>
            <strong style="font-family: var(--font-mono);">${b.id}</strong><br>
            <span style="font-size: 0.75rem; color: var(--slate-500);">${b.createdDate}</span>
          </td>
          <td>
            <strong>${b.equipmentName}</strong><br>
            <span style="font-size: 0.75rem; color: var(--slate-500);">${b.location}</span>
          </td>
          <td>
            ${b.startDate} to ${b.endDate}<br>
            <span style="font-size: 0.75rem; font-weight: 600; color: var(--dark-850);">${b.days} Days Contract</span>
          </td>
          <td>
            <strong style="font-family: var(--font-mono);">${this.formatCurrency(b.total)}</strong><br>
            <span style="font-size: 0.75rem; color: var(--slate-500);">${b.pickupType}</span>
          </td>
          <td>
            <span class="badge-status ${statusClass}">
              <span class="badge-dot"></span> ${b.status}
            </span>
          </td>
          <td>
            <div style="display: flex; gap: 0.4rem;">
              <button class="btn btn-outline btn-sm" onclick="app.downloadInvoice('${b.id}')">Contract PDF</button>
              ${b.status !== 'CANCELLED' ? `
                <button class="btn btn-danger-outline btn-sm" onclick="app.cancelBooking('${b.id}')">Cancel</button>
              ` : ''}
            </div>
          </td>
        </tr>
      `;
    }).join("");
  }

  renderAdminTab() {
    const bookings = this.getBookingsList();
    const equipment = this.getEquipmentList();

    const totalRev = bookings.filter(b => b.status !== "CANCELLED").reduce((acc, b) => acc + b.total, 0);
    const activeRentals = bookings.filter(b => b.status === "APPROVED" || b.status === "ACTIVE").length;
    const pendingApprovals = bookings.filter(b => b.status === "PENDING").length;

    const revEl = document.getElementById("adminTotalRevenue");
    const fleetEl = document.getElementById("adminFleetTotal");
    const activeEl = document.getElementById("adminActiveRentals");
    const pendingEl = document.getElementById("adminPendingApprovals");

    if (revEl) revEl.textContent = this.formatCurrency(totalRev);
    if (fleetEl) fleetEl.textContent = equipment.length;
    if (activeEl) activeEl.textContent = activeRentals;
    if (pendingEl) pendingEl.textContent = pendingApprovals;

    const approvalTbody = document.getElementById("adminApprovalsTableBody");
    if (approvalTbody) {
      if (bookings.length === 0) {
        approvalTbody.innerHTML = `<tr><td colspan="6" style="text-align: center; color: var(--slate-500); padding: 2rem;">No contracts on record.</td></tr>`;
      } else {
        approvalTbody.innerHTML = bookings.map(b => {
          let statusClass = "available";
          if (b.status === "PENDING") statusClass = "rented";
          if (b.status === "CANCELLED") statusClass = "cancelled";

          return `
            <tr>
              <td><strong style="font-family: var(--font-mono);">${b.id}</strong></td>
              <td>${b.customerName}<br><span style="font-size: 0.75rem; color: var(--slate-500);">${b.customerEmail}</span></td>
              <td><strong>${b.equipmentName}</strong></td>
              <td>${b.startDate} to ${b.endDate} (${b.days}d)</td>
              <td><strong style="font-family: var(--font-mono);">${this.formatCurrency(b.total)}</strong></td>
              <td><span class="badge-status ${statusClass}">${b.status}</span></td>
              <td>
                <div style="display: flex; gap: 0.35rem;">
                  ${b.status === 'PENDING' ? `
                    <button class="btn btn-success btn-sm" onclick="app.approveBooking('${b.id}')">Approve</button>
                    <button class="btn btn-danger-outline btn-sm" onclick="app.rejectBooking('${b.id}')">Decline</button>
                  ` : `
                    <span style="font-size: 0.75rem; color: var(--slate-500);">Confirmed</span>
                  `}
                </div>
              </td>
            </tr>
          `;
        }).join("");
      }
    }

    const fleetTbody = document.getElementById("adminFleetTableBody");
    if (fleetTbody) {
      fleetTbody.innerHTML = equipment.map(eq => `
        <tr>
          <td><strong style="font-family: var(--font-mono);">${eq.code}</strong></td>
          <td>
            <strong>${eq.name}</strong><br>
            <span style="font-size: 0.75rem; color: var(--slate-500);">${eq.categoryName} • ${eq.manufacturer}</span>
          </td>
          <td>${eq.locationName}</td>
          <td><strong style="font-family: var(--font-mono);">${this.formatCurrency(eq.dailyRate)}</strong></td>
          <td>
            <select class="select-compact" style="padding: 0.25rem 0.5rem; font-size: 0.78rem; min-width: 140px;" 
                    onchange="app.toggleEquipmentStatus('${eq.id}', this.value)">
              <option value="AVAILABLE" ${eq.status === 'AVAILABLE' ? 'selected' : ''}>Available</option>
              <option value="RENTED" ${eq.status === 'RENTED' ? 'selected' : ''}>On Lease</option>
              <option value="MAINTENANCE" ${eq.status === 'MAINTENANCE' ? 'selected' : ''}>Maintenance</option>
            </select>
          </td>
          <td>
            <button class="btn btn-outline btn-sm" onclick="app.openEquipmentModal('${eq.id}')">Specs</button>
          </td>
        </tr>
      `).join("");
    }
  }

  bindEvents() {
    document.querySelectorAll(".role-pill").forEach(btn => {
      btn.addEventListener("click", () => this.setRole(btn.dataset.role));
    });

    document.querySelectorAll(".tab-btn").forEach(tab => {
      tab.addEventListener("click", () => this.switchTab(tab.dataset.tab));
    });

    const searchInput = document.getElementById("catalogSearchInput");
    if (searchInput) {
      searchInput.addEventListener("input", (e) => {
        this.filters.search = e.target.value;
        this.renderBrowseTab();
      });
    }

    document.querySelectorAll(".filter-category-btn").forEach(pill => {
      pill.addEventListener("click", () => {
        document.querySelectorAll(".filter-category-btn").forEach(p => p.classList.remove("active"));
        pill.classList.add("active");
        this.filters.category = pill.dataset.category;
        this.renderBrowseTab();
      });
    });

    const statusSelect = document.getElementById("catalogStatusFilter");
    if (statusSelect) {
      statusSelect.addEventListener("change", (e) => {
        this.filters.status = e.target.value;
        this.renderBrowseTab();
      });
    }

    const locationSelect = document.getElementById("catalogLocationFilter");
    if (locationSelect) {
      locationSelect.addEventListener("change", (e) => {
        this.filters.location = e.target.value;
        this.renderBrowseTab();
      });
    }

    const sortSelect = document.getElementById("catalogSortFilter");
    if (sortSelect) {
      sortSelect.addEventListener("change", (e) => {
        this.filters.sortBy = e.target.value;
        this.renderBrowseTab();
      });
    }

    const startInput = document.getElementById("bookingStartDate");
    const endInput = document.getElementById("bookingEndDate");
    if (startInput && endInput) {
      startInput.addEventListener("change", () => this.updateModalCalculation());
      endInput.addEventListener("change", () => this.updateModalCalculation());
    }
  }
}

let app;
document.addEventListener("DOMContentLoaded", () => {
  app = new EquipTrackApp();
});
