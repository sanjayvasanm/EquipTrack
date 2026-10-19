/**
 * EquipTrack - Interactive Fleet Management & Rental Showcase
 * State Management, Reactive Filters, Booking Engine & Admin Operations
 */

// --- Initial Demo Data ---
const DEFAULT_CATEGORIES = [
  { id: "CAT-HM", name: "Heavy Machinery", icon: "🚜", count: 4 },
  { id: "CAT-MH", name: "Material Handling", icon: "🏗️", count: 2 },
  { id: "CAT-AL", name: "Aerial Lifts", icon: "🪜", count: 1 },
  { id: "CAT-CE", name: "Concrete Equipment", icon: "🚚", count: 1 },
  { id: "CAT-CP", name: "Compaction Equipment", icon: "🚜", count: 1 },
  { id: "CAT-PT", name: "Power & Generators", icon: "⚡", count: 1 }
];

const DEFAULT_LOCATIONS = [
  { id: "LOC-NY", name: "Warehouse A (East Hub)", city: "New York, NY", address: "123 Industrial Blvd", phone: "+1 (555) 100-1000", hours: "Mon-Sat: 7 AM - 7 PM" },
  { id: "LOC-LA", name: "Warehouse B (West Hub)", city: "Los Angeles, CA", address: "456 Commerce St", phone: "+1 (555) 200-2000", hours: "Mon-Sat: 6 AM - 8 PM" },
  { id: "LOC-CH", name: "Midwest Service Depot", city: "Chicago, IL", address: "789 Service Ave", phone: "+1 (555) 300-3000", hours: "Mon-Fri: 8 AM - 6 PM" }
];

const SVG_ICONS = {
  excavator: `<svg viewBox="0 0 160 120" width="120" height="90" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M20 90h110v18H20z" fill="#333948" rx="4"/>
    <circle cx="35" cy="99" r="6" fill="#FFC300"/>
    <circle cx="65" cy="99" r="6" fill="#FFC300"/>
    <circle cx="95" cy="99" r="6" fill="#FFC300"/>
    <circle cx="120" cy="99" r="6" fill="#FFC300"/>
    <path d="M35 60h45v30H35z" fill="#FFC300" rx="3"/>
    <path d="M42 66h25v16H42z" fill="#1E293B"/>
    <path d="M75 75l45-35 25 15-10 20" stroke="#FFC300" stroke-width="8" stroke-linecap="round" stroke-linejoin="round"/>
    <path d="M135 75l15 15h-20z" fill="#FFB000"/>
  </svg>`,
  bulldozer: `<svg viewBox="0 0 160 120" width="120" height="90" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M15 85h115v22H15z" fill="#333948" rx="4"/>
    <circle cx="30" cy="96" r="7" fill="#FFC300"/>
    <circle cx="58" cy="96" r="7" fill="#FFC300"/>
    <circle cx="86" cy="96" r="7" fill="#FFC300"/>
    <circle cx="115" cy="96" r="7" fill="#FFC300"/>
    <path d="M40 50h45v35H40z" fill="#FFC300" rx="4"/>
    <path d="M48 56h25v18H48z" fill="#1E293B"/>
    <path d="M125 55l15 45-8 5-15-45z" fill="#FFB000"/>
    <path d="M140 60v40" stroke="#E2E8F0" stroke-width="6" stroke-linecap="round"/>
  </svg>`,
  loader: `<svg viewBox="0 0 160 120" width="120" height="90" fill="none" xmlns="http://www.w3.org/2000/svg">
    <circle cx="40" cy="92" r="16" fill="#333948"/>
    <circle cx="40" cy="92" r="7" fill="#FFC300"/>
    <circle cx="115" cy="92" r="16" fill="#333948"/>
    <circle cx="115" cy="92" r="7" fill="#FFC300"/>
    <path d="M40 60h45v25H40z" fill="#FFC300" rx="3"/>
    <path d="M48 66h22v14H48z" fill="#1E293B"/>
    <path d="M85 75l45-15 15 30h-15" stroke="#FFC300" stroke-width="7" stroke-linecap="round"/>
  </svg>`,
  forklift: `<svg viewBox="0 0 160 120" width="120" height="90" fill="none" xmlns="http://www.w3.org/2000/svg">
    <circle cx="45" cy="95" r="14" fill="#333948"/>
    <circle cx="100" cy="95" r="14" fill="#333948"/>
    <path d="M30 55h40v35H30z" fill="#FFC300" rx="4"/>
    <path d="M35 62h18v18H35z" fill="#1E293B"/>
    <path d="M110 35v60M110 95h35" stroke="#FFB000" stroke-width="6" stroke-linecap="round"/>
  </svg>`,
  crane: `<svg viewBox="0 0 160 120" width="120" height="90" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M25 85h100v20H25z" fill="#333948" rx="4"/>
    <circle cx="45" cy="95" r="8" fill="#FFC300"/>
    <circle cx="75" cy="95" r="8" fill="#FFC300"/>
    <circle cx="105" cy="95" r="8" fill="#FFC300"/>
    <path d="M45 55h35v30H45z" fill="#FFC300" rx="3"/>
    <path d="M75 60l65-40" stroke="#FFC300" stroke-width="6" stroke-linecap="round"/>
    <path d="M140 20v45" stroke="#E2E8F0" stroke-width="3" stroke-dasharray="3,3"/>
    <circle cx="140" cy="68" r="4" fill="#E2E8F0"/>
  </svg>`,
  roller: `<svg viewBox="0 0 160 120" width="120" height="90" fill="none" xmlns="http://www.w3.org/2000/svg">
    <circle cx="120" cy="85" r="22" fill="#64748B" stroke="#333948" stroke-width="6"/>
    <circle cx="45" cy="90" r="16" fill="#333948"/>
    <path d="M45 60h45v25H45z" fill="#FFC300" rx="3"/>
    <path d="M52 66h22v14H52z" fill="#1E293B"/>
    <path d="M90 75h30" stroke="#FFB000" stroke-width="8" stroke-linecap="round"/>
  </svg>`,
  generator: `<svg viewBox="0 0 160 120" width="120" height="90" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect x="35" y="45" width="90" height="55" rx="6" fill="#FFC300" stroke="#333948" stroke-width="4"/>
    <rect x="45" y="55" width="30" height="20" rx="2" fill="#1E293B"/>
    <circle cx="100" cy="65" r="12" fill="#333948"/>
    <path d="M96 65h8M100 61v8" stroke="#10B981" stroke-width="2"/>
    <rect x="45" y="85" width="70" height="6" fill="#333948" rx="2"/>
  </svg>`
};

const DEFAULT_EQUIPMENT = [
  {
    id: "EQ000001",
    name: "Excavator CAT 320",
    code: "EQ000001",
    category: "CAT-HM",
    categoryName: "Heavy Machinery",
    location: "LOC-NY",
    locationName: "Warehouse A (East Hub)",
    dailyRate: 38250,
    weeklyRate: 238000,
    securityDeposit: 170000,
    status: "AVAILABLE",
    condition: "EXCELLENT",
    manufacturer: "Caterpillar",
    model: "320 D3",
    year: 2021,
    operatingWeight: "22.5 Tonnes",
    horsepower: "174 HP",
    digDepth: "6.72 m",
    description: "Next-generation hydraulic excavator engineered for high-production digging, bulk excavation, and trenching.",
    svgType: "excavator",
    featured: true
  },
  {
    id: "EQ000002",
    name: "Bulldozer CAT D6",
    code: "EQ000002",
    category: "CAT-HM",
    categoryName: "Heavy Machinery",
    location: "LOC-NY",
    locationName: "Warehouse A (East Hub)",
    dailyRate: 46750,
    weeklyRate: 297500,
    securityDeposit: 212500,
    status: "AVAILABLE",
    condition: "GOOD",
    manufacturer: "Caterpillar",
    model: "D6 T-Series",
    year: 2020,
    operatingWeight: "19.8 Tonnes",
    horsepower: "215 HP",
    digDepth: "Blade Capacity 5.6 m³",
    description: "High-horsepower crawler tractor offering class-leading pushing power, precision grade control, and heavy earthmoving.",
    svgType: "bulldozer",
    featured: true
  },
  {
    id: "EQ000003",
    name: "Backhoe Loader JCB 3DX",
    code: "EQ000003",
    category: "CAT-HM",
    categoryName: "Heavy Machinery",
    location: "LOC-LA",
    locationName: "Warehouse B (West Hub)",
    dailyRate: 29750,
    weeklyRate: 191250,
    securityDeposit: 127500,
    status: "RENTED",
    condition: "EXCELLENT",
    manufacturer: "JCB",
    model: "3DX Super",
    year: 2022,
    operatingWeight: "7.6 Tonnes",
    horsepower: "76 HP",
    digDepth: "4.77 m",
    description: "The most versatile dual-purpose loading and digging machine for road repair, utility work, and urban infrastructure.",
    svgType: "loader",
    featured: true
  },
  {
    id: "EQ000004",
    name: "Skid Steer Bobcat S590",
    code: "EQ000004",
    category: "CAT-MH",
    categoryName: "Material Handling",
    location: "LOC-LA",
    locationName: "Warehouse B (West Hub)",
    dailyRate: 18500,
    weeklyRate: 118000,
    securityDeposit: 75000,
    status: "AVAILABLE",
    condition: "EXCELLENT",
    manufacturer: "Bobcat",
    model: "S590 Vertical Lift",
    year: 2023,
    operatingWeight: "3.1 Tonnes",
    horsepower: "68 HP",
    digDepth: "Dump Height 3.0 m",
    description: "Compact agile loader with vertical lift path designed for loading high-sided trucks and tight job site maneuvers.",
    svgType: "loader",
    featured: false
  },
  {
    id: "EQ000005",
    name: "Forklift Toyota 8FDU30",
    code: "EQ000005",
    category: "CAT-MH",
    categoryName: "Material Handling",
    location: "LOC-CH",
    locationName: "Midwest Service Depot",
    dailyRate: 14200,
    weeklyRate: 89000,
    securityDeposit: 60000,
    status: "AVAILABLE",
    condition: "EXCELLENT",
    manufacturer: "Toyota",
    model: "8FDU30 Diesel",
    year: 2021,
    operatingWeight: "4.2 Tonnes",
    horsepower: "55 HP",
    digDepth: "Lift Height 4.8 m",
    description: "Heavy-duty 3-ton pneumatic tire forklift optimized for construction lumber yards, pipe stacking, and outdoor material logistics.",
    svgType: "forklift",
    featured: false
  },
  {
    id: "EQ000006",
    name: "Telescopic Boom Lift Genie S-85",
    code: "EQ000006",
    category: "CAT-AL",
    categoryName: "Aerial Lifts",
    location: "LOC-NY",
    locationName: "Warehouse A (East Hub)",
    dailyRate: 32000,
    weeklyRate: 205000,
    securityDeposit: 140000,
    status: "AVAILABLE",
    condition: "EXCELLENT",
    manufacturer: "Genie",
    model: "S-85 4WD",
    year: 2022,
    operatingWeight: "17.9 Tonnes",
    horsepower: "74 HP",
    digDepth: "Working Height 27.9 m",
    description: "Self-propelled telescopic boom with 360-degree continuous rotation for structural steel erection and industrial cladding.",
    svgType: "crane",
    featured: true
  },
  {
    id: "EQ000007",
    name: "Road Roller Bomag BW 211",
    code: "EQ000007",
    category: "CAT-CP",
    categoryName: "Compaction Equipment",
    location: "LOC-LA",
    locationName: "Warehouse B (West Hub)",
    dailyRate: 26500,
    weeklyRate: 168000,
    securityDeposit: 110000,
    status: "MAINTENANCE",
    condition: "GOOD",
    manufacturer: "Bomag",
    model: "BW 211 D-5",
    year: 2019,
    operatingWeight: "11.2 Tonnes",
    horsepower: "128 HP",
    digDepth: "Drum Width 2.13 m",
    description: "Single-drum vibratory compactor for soil stabilization, highway sub-bases, and large-scale industrial paving.",
    svgType: "roller",
    featured: false
  },
  {
    id: "EQ000008",
    name: "Cummins 125kVA Generator",
    code: "EQ000008",
    category: "CAT-PT",
    categoryName: "Power & Generators",
    location: "LOC-CH",
    locationName: "Midwest Service Depot",
    dailyRate: 12500,
    weeklyRate: 79000,
    securityDeposit: 50000,
    status: "AVAILABLE",
    condition: "EXCELLENT",
    manufacturer: "Cummins",
    model: "C125D5 Silent",
    year: 2023,
    operatingWeight: "1.9 Tonnes",
    horsepower: "150 HP Engine",
    digDepth: "Output 100 kW / 3 Phase",
    description: "Acoustic-enclosed ultra-silent diesel generator with automated transfer switch for continuous heavy-duty jobsite power.",
    svgType: "generator",
    featured: false
  }
];

const DEFAULT_BOOKINGS = [
  {
    id: "EQT-2026-9811",
    equipmentId: "EQ000001",
    equipmentName: "Excavator CAT 320",
    customerName: "Rajesh Sharma",
    customerEmail: "customer@test.com",
    startDate: "2026-10-25",
    endDate: "2026-10-30",
    days: 6,
    subtotal: 229500,
    tax: 11475,
    total: 240975,
    location: "Warehouse A (East Hub)",
    status: "APPROVED",
    pickupType: "Heavy Delivery",
    operatorRequested: true,
    createdDate: "2026-10-18"
  },
  {
    id: "EQT-2026-9812",
    equipmentId: "EQ000003",
    equipmentName: "Backhoe Loader JCB 3DX",
    customerName: "Metro Infrastructure Ltd",
    customerEmail: "logistics@metroinfra.in",
    startDate: "2026-10-15",
    endDate: "2026-11-05",
    days: 22,
    subtotal: 654500,
    tax: 32725,
    total: 687225,
    location: "Warehouse B (West Hub)",
    status: "ACTIVE",
    pickupType: "Warehouse Pickup",
    operatorRequested: false,
    createdDate: "2026-10-12"
  },
  {
    id: "EQT-2026-9813",
    equipmentId: "EQ000006",
    equipmentName: "Telescopic Boom Lift Genie S-85",
    customerName: "Rajesh Sharma",
    customerEmail: "customer@test.com",
    startDate: "2026-11-01",
    endDate: "2026-11-04",
    days: 4,
    subtotal: 128000,
    tax: 6400,
    total: 134400,
    location: "Warehouse A (East Hub)",
    status: "PENDING",
    pickupType: "Warehouse Pickup",
    operatorRequested: false,
    createdDate: "2026-10-19"
  }
];

// --- Application State Manager ---
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
    this.showToast("Demo database successfully restored to factory defaults!", "success");
    this.render();
  }

  setRole(newRole) {
    this.currentRole = newRole;
    localStorage.setItem("et_active_role", newRole);
    
    // Auto switch tab if permissions change
    if (newRole === "guest" && (this.currentTab === "bookings" || this.currentTab === "admin")) {
      this.currentTab = "browse";
    } else if (newRole === "customer" && this.currentTab === "admin") {
      this.currentTab = "browse";
    }

    this.showToast(`Switched active persona to: ${newRole.toUpperCase()}`, "info");
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

  // --- Filtering & Sorting ---
  getFilteredEquipment() {
    let list = this.getEquipmentList();

    if (this.filters.search) {
      const q = this.filters.search.toLowerCase();
      list = list.filter(item => 
        item.name.toLowerCase().includes(q) ||
        item.manufacturer.toLowerCase().includes(q) ||
        item.model.toLowerCase().includes(q) ||
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
      // Featured / default
      list.sort((a, b) => (b.featured ? 1 : 0) - (a.featured ? 1 : 0));
    }

    return list;
  }

  // --- Date Calculation Engine ---
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

  // --- Modals ---
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
    document.getElementById("modalEqYear").textContent = eq.year || "2021";

    const svgSlot = document.getElementById("modalEqSvgSlot");
    svgSlot.innerHTML = SVG_ICONS[eq.svgType] || SVG_ICONS.loader;

    const statusBadge = document.getElementById("modalEqStatus");
    statusBadge.className = `status-chip ${eq.status.toLowerCase()}`;
    statusBadge.innerHTML = `<span class="status-chip-dot"></span> ${eq.status}`;

    // Dates
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
      bookBtn.textContent = `Reserve Now • ${this.formatCurrency(calc.total)}`;
    } else if (this.selectedEquipment.status !== "AVAILABLE") {
      bookBtn.disabled = true;
      bookBtn.textContent = `Machine is currently ${this.selectedEquipment.status}`;
    } else {
      bookBtn.disabled = true;
      bookBtn.textContent = "Select Valid Dates";
    }
  }

  proceedToCheckout() {
    if (this.currentRole === "guest") {
      this.closeEquipmentModal();
      this.showToast("Please switch to 'Customer Persona' to submit bookings!", "danger");
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
    document.getElementById("checkoutDuration").textContent = `${calc.days} Days (${document.getElementById("bookingStartDate").value} to ${document.getElementById("bookingEndDate").value})`;
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
      customerName: this.currentRole === "customer" ? "Rajesh Sharma" : "Guest User",
      customerEmail: "customer@test.com",
      startDate: startDate,
      endDate: endDate,
      days: calc.days,
      subtotal: calc.subtotal,
      tax: calc.tax,
      total: calc.total,
      location: eq.locationName,
      status: "APPROVED", // Auto-approved for instant satisfaction
      pickupType: delivery,
      operatorRequested: operator,
      notes: notes,
      createdDate: new Date().toISOString().split("T")[0]
    };

    const bookings = this.getBookingsList();
    bookings.unshift(newBooking);
    this.saveBookingsList(bookings);

    this.closeCheckoutModal();
    this.showToast(`🎉 Booking ${bookingId} successfully confirmed!`, "success");
    this.switchTab("bookings");
  }

  // --- Admin Operations ---
  approveBooking(bookingId) {
    const list = this.getBookingsList();
    const b = list.find(x => x.id === bookingId);
    if (b) {
      b.status = "APPROVED";
      this.saveBookingsList(list);
      this.showToast(`Booking ${bookingId} has been APPROVED!`, "success");
      this.render();
    }
  }

  rejectBooking(bookingId) {
    const list = this.getBookingsList();
    const b = list.find(x => x.id === bookingId);
    if (b) {
      b.status = "CANCELLED";
      this.saveBookingsList(list);
      this.showToast(`Booking ${bookingId} has been REJECTED.`, "danger");
      this.render();
    }
  }

  toggleEquipmentStatus(eqId, newStatus) {
    const list = this.getEquipmentList();
    const eq = list.find(x => x.id === eqId);
    if (eq) {
      eq.status = newStatus;
      this.saveEquipmentList(list);
      this.showToast(`${eq.name} status updated to ${newStatus}`, "info");
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
    const rate = Number(document.getElementById("newEqRate").value) || 20000;
    const mfg = document.getElementById("newEqMfg").value.trim() || "Caterpillar";
    const model = document.getElementById("newEqModel").value.trim() || "Pro";
    const desc = document.getElementById("newEqDesc").value.trim() || "Heavy machinery for professional earthworks.";

    const categoryObj = DEFAULT_CATEGORIES.find(c => c.id === category);
    const locationObj = DEFAULT_LOCATIONS.find(l => l.id === location);

    const newEq = {
      id: "EQ0000" + Math.floor(10 + Math.random() * 90),
      name: name,
      code: "EQ0000" + Math.floor(10 + Math.random() * 90),
      category: category,
      categoryName: categoryObj ? categoryObj.name : "Heavy Machinery",
      location: location,
      locationName: locationObj ? locationObj.name : "Warehouse A",
      dailyRate: rate,
      weeklyRate: Math.round(rate * 6),
      securityDeposit: Math.round(rate * 4),
      status: "AVAILABLE",
      condition: "EXCELLENT",
      manufacturer: mfg,
      model: model,
      year: 2023,
      operatingWeight: "15 Tonnes",
      horsepower: "140 HP",
      digDepth: "5.5 m",
      description: desc,
      svgType: "excavator",
      featured: true
    };

    const list = this.getEquipmentList();
    list.unshift(newEq);
    this.saveEquipmentList(list);

    this.closeAddEquipmentModal();
    this.showToast(`Machine ${name} added to live fleet!`, "success");
    this.render();
  }

  cancelBooking(bookingId) {
    if (confirm(`Are you sure you want to cancel booking ${bookingId}?`)) {
      const list = this.getBookingsList();
      const b = list.find(x => x.id === bookingId);
      if (b) {
        b.status = "CANCELLED";
        this.saveBookingsList(list);
        this.showToast(`Booking ${bookingId} cancelled successfully.`, "info");
        this.render();
      }
    }
  }

  downloadInvoice(bookingId) {
    const list = this.getBookingsList();
    const b = list.find(x => x.id === bookingId);
    if (!b) return;

    this.showToast(`Tax Invoice #${bookingId}.pdf generated and downloaded!`, "success");
  }

  showToast(message, type = "info") {
    const container = document.getElementById("toastContainer");
    if (!container) return;

    const toast = document.createElement("div");
    toast.className = `toast ${type}`;
    let icon = "ℹ️";
    if (type === "success") icon = "✅";
    if (type === "danger") icon = "⚠️";

    toast.innerHTML = `<span style="font-size: 1.2rem;">${icon}</span> <span>${message}</span>`;
    container.appendChild(toast);

    setTimeout(() => {
      toast.style.opacity = "0";
      toast.style.transform = "translateX(50px)";
      toast.style.transition = "all 0.3s";
      setTimeout(() => toast.remove(), 300);
    }, 3500);
  }

  // --- Rendering Functions ---
  render() {
    this.renderRoleBar();
    this.renderNavbar();
    this.renderActiveTab();
  }

  renderRoleBar() {
    document.querySelectorAll(".role-btn").forEach(btn => {
      btn.classList.toggle("active", btn.dataset.role === this.currentRole);
    });

    const activeLabel = document.getElementById("activePersonaLabel");
    if (activeLabel) {
      if (this.currentRole === "admin") {
        activeLabel.textContent = "Admin (Sanjay - Fleet Director)";
      } else if (this.currentRole === "customer") {
        activeLabel.textContent = "Customer (Rajesh Sharma)";
      } else {
        activeLabel.textContent = "Guest Visitor (Browse Mode)";
      }
    }
  }

  renderNavbar() {
    document.querySelectorAll(".nav-tab").forEach(tab => {
      tab.classList.toggle("active", tab.dataset.tab === this.currentTab);
    });

    // Badge counts
    const myBookings = this.getBookingsList().filter(b => b.customerEmail === "customer@test.com" && b.status !== "CANCELLED");
    const badge = document.getElementById("myBookingsBadge");
    if (badge) {
      badge.textContent = myBookings.length;
    }

    // Role visibility
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
    // Hide all tabs
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
      countBadge.textContent = `${filtered.length} Machines Available`;
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
    const svg = SVG_ICONS[eq.svgType] || SVG_ICONS.loader;
    const isAvail = eq.status === "AVAILABLE";

    return `
      <div class="equipment-card">
        <div class="equipment-card-media">
          <span class="equipment-category-tag">${eq.categoryName}</span>
          <div class="equipment-status-top">
            <span class="status-chip ${eq.status.toLowerCase()}">
              <span class="status-chip-dot"></span> ${eq.status}
            </span>
          </div>
          <div class="equipment-svg-container">
            ${svg}
          </div>
        </div>

        <div class="equipment-card-body">
          <div class="equipment-card-title-row">
            <div>
              <h3>${eq.name}</h3>
              <span class="equipment-code">${eq.manufacturer} • ${eq.model}</span>
            </div>
          </div>

          <p class="equipment-card-desc">${eq.description}</p>

          <div class="equipment-specs-grid">
            <div class="spec-cell">
              <span class="spec-key">Operating Wt</span>
              <span class="spec-val">${eq.operatingWeight || 'Standard'}</span>
            </div>
            <div class="spec-cell">
              <span class="spec-key">Horsepower</span>
              <span class="spec-val">${eq.horsepower || 'Heavy'}</span>
            </div>
            <div class="spec-cell">
              <span class="spec-key">Depot</span>
              <span class="spec-val">${eq.locationName.split('(')[0]}</span>
            </div>
            <div class="spec-cell">
              <span class="spec-key">Condition</span>
              <span class="spec-val" style="color: var(--success);">${eq.condition}</span>
            </div>
          </div>

          <div class="equipment-pricing-footer">
            <div class="price-box">
              <span class="price-sub">Daily Base Rate</span>
              <span class="price-main">${this.formatCurrency(eq.dailyRate)} <small>/day</small></span>
            </div>
            <button class="btn ${isAvail ? 'btn-primary' : 'btn-outline'}" 
                    onclick="app.openEquipmentModal('${eq.id}')">
              ${isAvail ? 'Reserve Fleet ⚡' : 'View Specs'}
            </button>
          </div>
        </div>
      </div>
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
      if (b.status === "CANCELLED") statusClass = "maintenance";

      return `
        <tr>
          <td>
            <strong>${b.id}</strong><br>
            <small style="color: var(--gray-500);">${b.createdDate}</small>
          </td>
          <td>
            <strong>${b.equipmentName}</strong><br>
            <small style="color: var(--gray-500);">📍 ${b.location}</small>
          </td>
          <td>
            ${b.startDate} to ${b.endDate}<br>
            <span style="font-size: 0.8rem; font-weight: 700; color: var(--primary-dark);">${b.days} Days</span>
          </td>
          <td>
            <strong>${this.formatCurrency(b.total)}</strong><br>
            <small style="color: var(--gray-500);">${b.pickupType}</small>
          </td>
          <td>
            <span class="status-chip ${statusClass}">
              <span class="status-chip-dot"></span> ${b.status}
            </span>
          </td>
          <td>
            <div style="display: flex; gap: 0.4rem;">
              <button class="btn btn-outline btn-sm" onclick="app.downloadInvoice('${b.id}')">📄 Receipt</button>
              ${b.status !== 'CANCELLED' ? `
                <button class="btn btn-danger btn-sm" onclick="app.cancelBooking('${b.id}')">Cancel</button>
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

    // Stats
    const totalRev = bookings.filter(b => b.status !== "CANCELLED").reduce((acc, b) => acc + b.total, 0);
    const activeRentals = bookings.filter(b => b.status === "APPROVED" || b.status === "ACTIVE").length;
    const maintenanceCount = equipment.filter(e => e.status === "MAINTENANCE").length;
    const pendingApprovals = bookings.filter(b => b.status === "PENDING").length;

    const revEl = document.getElementById("adminTotalRevenue");
    const fleetEl = document.getElementById("adminFleetTotal");
    const activeEl = document.getElementById("adminActiveRentals");
    const pendingEl = document.getElementById("adminPendingApprovals");

    if (revEl) revEl.textContent = this.formatCurrency(totalRev);
    if (fleetEl) fleetEl.textContent = equipment.length;
    if (activeEl) activeEl.textContent = activeRentals;
    if (pendingEl) pendingEl.textContent = pendingApprovals;

    // Approvals Table
    const approvalTbody = document.getElementById("adminApprovalsTableBody");
    if (approvalTbody) {
      if (bookings.length === 0) {
        approvalTbody.innerHTML = `<tr><td colspan="6" style="text-align: center; color: var(--gray-500); padding: 2rem;">No bookings recorded yet.</td></tr>`;
      } else {
        approvalTbody.innerHTML = bookings.map(b => {
          let statusClass = "available";
          if (b.status === "PENDING") statusClass = "rented";
          if (b.status === "CANCELLED") statusClass = "maintenance";

          return `
            <tr>
              <td><strong>${b.id}</strong></td>
              <td>${b.customerName}<br><small style="color: var(--gray-500);">${b.customerEmail}</small></td>
              <td><strong>${b.equipmentName}</strong></td>
              <td>${b.startDate} to ${b.endDate} (${b.days}d)</td>
              <td><strong>${this.formatCurrency(b.total)}</strong></td>
              <td>
                <span class="status-chip ${statusClass}">${b.status}</span>
              </td>
              <td>
                <div style="display: flex; gap: 0.35rem;">
                  ${b.status === 'PENDING' ? `
                    <button class="btn btn-success btn-sm" onclick="app.approveBooking('${b.id}')">✓ Approve</button>
                    <button class="btn btn-danger btn-sm" onclick="app.rejectBooking('${b.id}')">✕ Reject</button>
                  ` : `
                    <span style="font-size: 0.8rem; color: var(--gray-500);">Completed</span>
                  `}
                </div>
              </td>
            </tr>
          `;
        }).join("");
      }
    }

    // Equipment Fleet Table
    const fleetTbody = document.getElementById("adminFleetTableBody");
    if (fleetTbody) {
      fleetTbody.innerHTML = equipment.map(eq => `
        <tr>
          <td><strong>${eq.code}</strong></td>
          <td>
            <strong>${eq.name}</strong><br>
            <small style="color: var(--gray-500);">${eq.categoryName} • ${eq.manufacturer}</small>
          </td>
          <td>${eq.locationName.split('(')[0]}</td>
          <td><strong>${this.formatCurrency(eq.dailyRate)}/d</strong></td>
          <td>
            <select class="form-control" style="padding: 0.3rem 0.5rem; font-size: 0.82rem; width: auto;" 
                    onchange="app.toggleEquipmentStatus('${eq.id}', this.value)">
              <option value="AVAILABLE" ${eq.status === 'AVAILABLE' ? 'selected' : ''}>🟢 AVAILABLE</option>
              <option value="RENTED" ${eq.status === 'RENTED' ? 'selected' : ''}>🟠 RENTED</option>
              <option value="MAINTENANCE" ${eq.status === 'MAINTENANCE' ? 'selected' : ''}>🔴 MAINTENANCE</option>
            </select>
          </td>
          <td>
            <button class="btn btn-outline btn-sm" onclick="app.openEquipmentModal('${eq.id}')">View Details</button>
          </td>
        </tr>
      `).join("");
    }
  }

  bindEvents() {
    // Role switcher
    document.querySelectorAll(".role-btn").forEach(btn => {
      btn.addEventListener("click", () => this.setRole(btn.dataset.role));
    });

    // Navigation tabs
    document.querySelectorAll(".nav-tab").forEach(tab => {
      tab.addEventListener("click", () => this.switchTab(tab.dataset.tab));
    });

    // Search bar
    const searchInput = document.getElementById("catalogSearchInput");
    if (searchInput) {
      searchInput.addEventListener("input", (e) => {
        this.filters.search = e.target.value;
        this.renderBrowseTab();
      });
    }

    // Category pills
    document.querySelectorAll(".category-pill").forEach(pill => {
      pill.addEventListener("click", () => {
        document.querySelectorAll(".category-pill").forEach(p => p.classList.remove("active"));
        pill.classList.add("active");
        this.filters.category = pill.dataset.category;
        this.renderBrowseTab();
      });
    });

    // Status filter
    const statusSelect = document.getElementById("catalogStatusFilter");
    if (statusSelect) {
      statusSelect.addEventListener("change", (e) => {
        this.filters.status = e.target.value;
        this.renderBrowseTab();
      });
    }

    // Location filter
    const locationSelect = document.getElementById("catalogLocationFilter");
    if (locationSelect) {
      locationSelect.addEventListener("change", (e) => {
        this.filters.location = e.target.value;
        this.renderBrowseTab();
      });
    }

    // Sort filter
    const sortSelect = document.getElementById("catalogSortFilter");
    if (sortSelect) {
      sortSelect.addEventListener("change", (e) => {
        this.filters.sortBy = e.target.value;
        this.renderBrowseTab();
      });
    }

    // Modal date change events
    const startInput = document.getElementById("bookingStartDate");
    const endInput = document.getElementById("bookingEndDate");
    if (startInput && endInput) {
      startInput.addEventListener("change", () => this.updateModalCalculation());
      endInput.addEventListener("change", () => this.updateModalCalculation());
    }
  }
}

// Instantiate global app
let app;
document.addEventListener("DOMContentLoaded", () => {
  app = new EquipTrackApp();
});
