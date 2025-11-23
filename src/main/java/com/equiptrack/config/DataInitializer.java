package com.equiptrack.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.equiptrack.model.Category;
import com.equiptrack.model.Equipment;
import com.equiptrack.model.Location;
import com.equiptrack.model.User;
import com.equiptrack.repository.CategoryRepository;
import com.equiptrack.repository.EquipmentRepository;
import com.equiptrack.repository.LocationRepository;
import com.equiptrack.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Data initializer to populate the database with sample data
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final EquipmentRepository equipmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create admin user
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setEmail("admin@equiptrack.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Admin User");
            admin.setRole(User.UserRole.ADMIN);
            admin.setStatus(User.AccountStatus.ACTIVE);
            admin.setEmailVerified(true);
            userRepository.save(admin);
            log.info("Admin user created");

            // Create test customer
            User customer = new User();
            customer.setEmail("customer@test.com");
            customer.setPassword(passwordEncoder.encode("customer123"));
            customer.setFullName("Test Customer");
            customer.setPhoneNumber("+1 (555) 123-4567");
            customer.setRole(User.UserRole.CUSTOMER);
            customer.setStatus(User.AccountStatus.ACTIVE);
            customer.setEmailVerified(true);
            userRepository.save(customer);
            log.info("Test customer created");
        }

        // Create categories
        if (categoryRepository.count() == 0) {
            Category heavyMachinery = new Category();
            heavyMachinery.setName("Heavy Machinery");
            heavyMachinery.setCode("HM");
            heavyMachinery.setDescription("Construction and excavation equipment");
            heavyMachinery.setIsActive(true);
            heavyMachinery.setDisplayOrder(1);
            categoryRepository.save(heavyMachinery);

            Category materialHandling = new Category();
            materialHandling.setName("Material Handling");
            materialHandling.setCode("MH");
            materialHandling.setDescription("Forklifts and loading equipment");
            materialHandling.setIsActive(true);
            materialHandling.setDisplayOrder(2);
            categoryRepository.save(materialHandling);

            Category aerialLifts = new Category();
            aerialLifts.setName("Aerial Lifts");
            aerialLifts.setCode("AL");
            aerialLifts.setDescription("Scissor lifts and aerial platforms");
            aerialLifts.setIsActive(true);
            aerialLifts.setDisplayOrder(3);
            categoryRepository.save(aerialLifts);

            Category concreteEquipment = new Category();
            concreteEquipment.setName("Concrete Equipment");
            concreteEquipment.setCode("CE");
            concreteEquipment.setDescription("Mixers, pumps, and concrete tools");
            concreteEquipment.setIsActive(true);
            concreteEquipment.setDisplayOrder(4);
            categoryRepository.save(concreteEquipment);

            Category compactionEquipment = new Category();
            compactionEquipment.setName("Compaction Equipment");
            compactionEquipment.setCode("CP");
            compactionEquipment.setDescription("Compactors and rollers");
            compactionEquipment.setIsActive(true);
            compactionEquipment.setDisplayOrder(5);
            categoryRepository.save(compactionEquipment);

            Category powerTools = new Category();
            powerTools.setName("Power Tools & Generators");
            powerTools.setCode("PT");
            powerTools.setDescription("Generators, welding equipment, and power tools");
            powerTools.setIsActive(true);
            powerTools.setDisplayOrder(6);
            categoryRepository.save(powerTools);

            log.info("Categories created");
        }

        // Create locations
        if (locationRepository.count() == 0) {
            Location warehouseA = new Location();
            warehouseA.setName("Warehouse A");
            warehouseA.setCode("WHA");
            warehouseA.setType(Location.LocationType.WAREHOUSE);
            warehouseA.setAddress("123 Industrial Blvd");
            warehouseA.setCity("New York");
            warehouseA.setState("NY");
            warehouseA.setZipCode("10001");
            warehouseA.setPhoneNumber("+1 (555) 100-1000");
            warehouseA.setIsActive(true);
            warehouseA.setSupportsPickup(true);
            warehouseA.setSupportsDelivery(true);
            locationRepository.save(warehouseA);

            Location warehouseB = new Location();
            warehouseB.setName("Warehouse B");
            warehouseB.setCode("WHB");
            warehouseB.setType(Location.LocationType.WAREHOUSE);
            warehouseB.setAddress("456 Commerce St");
            warehouseB.setCity("Los Angeles");
            warehouseB.setState("CA");
            warehouseB.setZipCode("90001");
            warehouseB.setPhoneNumber("+1 (555) 200-2000");
            warehouseB.setIsActive(true);
            warehouseB.setSupportsPickup(true);
            warehouseB.setSupportsDelivery(true);
            locationRepository.save(warehouseB);

            Location serviceCenter = new Location();
            serviceCenter.setName("Service Center");
            serviceCenter.setCode("SC1");
            serviceCenter.setType(Location.LocationType.SERVICE_CENTER);
            serviceCenter.setAddress("789 Service Ave");
            serviceCenter.setCity("Chicago");
            serviceCenter.setState("IL");
            serviceCenter.setZipCode("60601");
            serviceCenter.setPhoneNumber("+1 (555) 300-3000");
            serviceCenter.setIsActive(true);
            serviceCenter.setSupportsPickup(true);
            serviceCenter.setSupportsDelivery(false);
            locationRepository.save(serviceCenter);

            log.info("Locations created");
        }

        // Create sample equipment
        if (equipmentRepository.count() == 0) {
            Category heavyMachinery = categoryRepository.findByCode("HM").orElseThrow();
            Category materialHandling = categoryRepository.findByCode("MH").orElseThrow();
            Category aerialLifts = categoryRepository.findByCode("AL").orElseThrow();
            Category concreteEquipment = categoryRepository.findByCode("CE").orElseThrow();
            Category compactionEquipment = categoryRepository.findByCode("CP").orElseThrow();
            Category powerTools = categoryRepository.findByCode("PT").orElseThrow();
            Location warehouseA = locationRepository.findByCode("WHA").orElseThrow();
            Location warehouseB = locationRepository.findByCode("WHB").orElseThrow();
            Location serviceCenter = locationRepository.findByCode("SC1").orElseThrow();

            // HEAVY MACHINERY
            Equipment excavator = new Equipment();
            excavator.setEquipmentCode("EQ000001");
            excavator.setName("Excavator CAT 320");
            excavator.setDescription("Heavy-duty excavator for construction projects. Perfect for digging, trenching, and material handling.");
            excavator.setCategoryId(heavyMachinery.getId());
            excavator.setLocationId(warehouseA.getId());
            excavator.setDailyRate(new BigDecimal("38250"));
            excavator.setWeeklyRate(new BigDecimal("238000"));
            excavator.setMonthlyRate(new BigDecimal("850000"));
            excavator.setStatus(Equipment.EquipmentStatus.MAINTENANCE);
            excavator.setCondition(Equipment.EquipmentCondition.EXCELLENT);
            excavator.setManufacturer("Caterpillar");
            excavator.setModel("320");
            excavator.setYearOfManufacture(2020);
            excavator.setIsActive(true);
            excavator.setIsFeatured(true);
            excavator.setSecurityDeposit(new BigDecimal("170000"));
            equipmentRepository.save(excavator);

            Equipment bulldozer = new Equipment();
            bulldozer.setEquipmentCode("EQ000002");
            bulldozer.setName("Bulldozer CAT D6");
            bulldozer.setDescription("Powerful bulldozer for heavy earthmoving and grading operations.");
            bulldozer.setCategoryId(heavyMachinery.getId());
            bulldozer.setLocationId(warehouseA.getId());
            bulldozer.setDailyRate(new BigDecimal("46750"));
            bulldozer.setWeeklyRate(new BigDecimal("297500"));
            bulldozer.setMonthlyRate(new BigDecimal("1020000"));
            bulldozer.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            bulldozer.setCondition(Equipment.EquipmentCondition.GOOD);
            bulldozer.setManufacturer("Caterpillar");
            bulldozer.setModel("D6");
            bulldozer.setYearOfManufacture(2018);
            bulldozer.setIsActive(true);
            bulldozer.setIsFeatured(true);
            bulldozer.setSecurityDeposit(new BigDecimal("212500"));
            equipmentRepository.save(bulldozer);

            Equipment backhoeLoader = new Equipment();
            backhoeLoader.setEquipmentCode("EQ000003");
            backhoeLoader.setName("Backhoe Loader JCB 3DX");
            backhoeLoader.setDescription("Versatile backhoe loader for excavation and loading. Most popular construction equipment in India.");
            backhoeLoader.setCategoryId(heavyMachinery.getId());
            backhoeLoader.setLocationId(warehouseB.getId());
            backhoeLoader.setDailyRate(new BigDecimal("29750"));
            backhoeLoader.setWeeklyRate(new BigDecimal("191250"));
            backhoeLoader.setMonthlyRate(new BigDecimal("680000"));
            backhoeLoader.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            backhoeLoader.setCondition(Equipment.EquipmentCondition.EXCELLENT);
            backhoeLoader.setManufacturer("JCB");
            backhoeLoader.setModel("3DX");
            backhoeLoader.setYearOfManufacture(2021);
            backhoeLoader.setIsActive(true);
            backhoeLoader.setIsFeatured(true);
            backhoeLoader.setSecurityDeposit(new BigDecimal("127500"));
            equipmentRepository.save(backhoeLoader);

            Equipment grader = new Equipment();
            grader.setEquipmentCode("EQ000004");
            grader.setName("Motor Grader CAT 140M");
            grader.setDescription("Precision motor grader for road construction and grading operations.");
            grader.setCategoryId(heavyMachinery.getId());
            grader.setLocationId(warehouseA.getId());
            grader.setDailyRate(new BigDecimal("42500"));
            grader.setWeeklyRate(new BigDecimal("272000"));
            grader.setMonthlyRate(new BigDecimal("935000"));
            grader.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            grader.setCondition(Equipment.EquipmentCondition.GOOD);
            grader.setManufacturer("Caterpillar");
            grader.setModel("140M");
            grader.setYearOfManufacture(2019);
            grader.setIsActive(true);
            grader.setSecurityDeposit(new BigDecimal("191250"));
            equipmentRepository.save(grader);

            Equipment skidSteer = new Equipment();
            skidSteer.setEquipmentCode("EQ000005");
            skidSteer.setName("Skid Steer Loader Bobcat S650");
            skidSteer.setDescription("Compact and maneuverable skid steer loader for tight construction sites.");
            skidSteer.setCategoryId(heavyMachinery.getId());
            skidSteer.setLocationId(warehouseB.getId());
            skidSteer.setDailyRate(new BigDecimal("21250"));
            skidSteer.setWeeklyRate(new BigDecimal("136000"));
            skidSteer.setMonthlyRate(new BigDecimal("467500"));
            skidSteer.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            skidSteer.setCondition(Equipment.EquipmentCondition.EXCELLENT);
            skidSteer.setManufacturer("Bobcat");
            skidSteer.setModel("S650");
            skidSteer.setYearOfManufacture(2022);
            skidSteer.setIsActive(true);
            skidSteer.setSecurityDeposit(new BigDecimal("93500"));
            equipmentRepository.save(skidSteer);

            // MATERIAL HANDLING
            Equipment forklift = new Equipment();
            forklift.setEquipmentCode("EQ000006");
            forklift.setName("Forklift Toyota 8FG25");
            forklift.setDescription("Electric forklift with 2.5 ton capacity. Ideal for warehouse operations and loading.");
            forklift.setCategoryId(materialHandling.getId());
            forklift.setLocationId(warehouseB.getId());
            forklift.setDailyRate(new BigDecimal("10200"));
            forklift.setWeeklyRate(new BigDecimal("63750"));
            forklift.setMonthlyRate(new BigDecimal("238000"));
            forklift.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            forklift.setCondition(Equipment.EquipmentCondition.GOOD);
            forklift.setManufacturer("Toyota");
            forklift.setModel("8FG25");
            forklift.setYearOfManufacture(2019);
            forklift.setIsActive(true);
            forklift.setIsFeatured(true);
            forklift.setSecurityDeposit(new BigDecimal("42500"));
            equipmentRepository.save(forklift);

            Equipment telehandler = new Equipment();
            telehandler.setEquipmentCode("EQ000007");
            telehandler.setName("Telehandler JCB 540-170");
            telehandler.setDescription("Versatile telehandler with 17m reach. Great for construction and agricultural use.");
            telehandler.setCategoryId(materialHandling.getId());
            telehandler.setLocationId(warehouseB.getId());
            telehandler.setDailyRate(new BigDecimal("27200"));
            telehandler.setWeeklyRate(new BigDecimal("170000"));
            telehandler.setMonthlyRate(new BigDecimal("637500"));
            telehandler.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            telehandler.setCondition(Equipment.EquipmentCondition.EXCELLENT);
            telehandler.setManufacturer("JCB");
            telehandler.setModel("540-170");
            telehandler.setYearOfManufacture(2022);
            telehandler.setIsActive(true);
            telehandler.setIsFeatured(true);
            telehandler.setSecurityDeposit(new BigDecimal("127500"));
            equipmentRepository.save(telehandler);

            Equipment reachTruck = new Equipment();
            reachTruck.setEquipmentCode("EQ000008");
            reachTruck.setName("Reach Truck Crown RR5700");
            reachTruck.setDescription("High-reach warehouse forklift with excellent maneuverability for narrow aisles.");
            reachTruck.setCategoryId(materialHandling.getId());
            reachTruck.setLocationId(serviceCenter.getId());
            reachTruck.setDailyRate(new BigDecimal("12750"));
            reachTruck.setWeeklyRate(new BigDecimal("80750"));
            reachTruck.setMonthlyRate(new BigDecimal("297500"));
            reachTruck.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            reachTruck.setCondition(Equipment.EquipmentCondition.EXCELLENT);
            reachTruck.setManufacturer("Crown");
            reachTruck.setModel("RR5700");
            reachTruck.setYearOfManufacture(2021);
            reachTruck.setIsActive(true);
            reachTruck.setSecurityDeposit(new BigDecimal("59500"));
            equipmentRepository.save(reachTruck);

            Equipment palletJack = new Equipment();
            palletJack.setEquipmentCode("EQ000009");
            palletJack.setName("Electric Pallet Jack Crown WP3000");
            palletJack.setDescription("Electric pallet jack for efficient material movement in warehouses.");
            palletJack.setCategoryId(materialHandling.getId());
            palletJack.setLocationId(warehouseB.getId());
            palletJack.setDailyRate(new BigDecimal("5950"));
            palletJack.setWeeklyRate(new BigDecimal("38250"));
            palletJack.setMonthlyRate(new BigDecimal("136000"));
            palletJack.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            palletJack.setCondition(Equipment.EquipmentCondition.GOOD);
            palletJack.setManufacturer("Crown");
            palletJack.setModel("WP3000");
            palletJack.setYearOfManufacture(2020);
            palletJack.setIsActive(true);
            palletJack.setSecurityDeposit(new BigDecimal("25500"));
            equipmentRepository.save(palletJack);

            // AERIAL LIFTS
            Equipment scissorLift = new Equipment();
            scissorLift.setEquipmentCode("EQ000010");
            scissorLift.setName("Scissor Lift JLG 2630ES");
            scissorLift.setDescription("Electric scissor lift with 26ft platform height. Perfect for indoor maintenance work.");
            scissorLift.setCategoryId(aerialLifts.getId());
            scissorLift.setLocationId(serviceCenter.getId());
            scissorLift.setDailyRate(new BigDecimal("15300"));
            scissorLift.setWeeklyRate(new BigDecimal("93500"));
            scissorLift.setMonthlyRate(new BigDecimal("340000"));
            scissorLift.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            scissorLift.setCondition(Equipment.EquipmentCondition.EXCELLENT);
            scissorLift.setManufacturer("JLG");
            scissorLift.setModel("2630ES");
            scissorLift.setYearOfManufacture(2021);
            scissorLift.setIsActive(true);
            scissorLift.setIsFeatured(true);
            scissorLift.setSecurityDeposit(new BigDecimal("68000"));
            equipmentRepository.save(scissorLift);

            Equipment boomLift = new Equipment();
            boomLift.setEquipmentCode("EQ000011");
            boomLift.setName("Boom Lift Genie Z-45/25J");
            boomLift.setDescription("Articulating boom lift with 45ft working height for outdoor construction.");
            boomLift.setCategoryId(aerialLifts.getId());
            boomLift.setLocationId(warehouseA.getId());
            boomLift.setDailyRate(new BigDecimal("21250"));
            boomLift.setWeeklyRate(new BigDecimal("136000"));
            boomLift.setMonthlyRate(new BigDecimal("467500"));
            boomLift.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            boomLift.setCondition(Equipment.EquipmentCondition.EXCELLENT);
            boomLift.setManufacturer("Genie");
            boomLift.setModel("Z-45/25J");
            boomLift.setYearOfManufacture(2022);
            boomLift.setIsActive(true);
            boomLift.setSecurityDeposit(new BigDecimal("93500"));
            equipmentRepository.save(boomLift);

            Equipment cherryPicker = new Equipment();
            cherryPicker.setEquipmentCode("EQ000012");
            cherryPicker.setName("Vertical Mast Lift JLG 20MVL");
            cherryPicker.setDescription("Compact vertical mast lift for tight indoor spaces and retail environments.");
            cherryPicker.setCategoryId(aerialLifts.getId());
            cherryPicker.setLocationId(serviceCenter.getId());
            cherryPicker.setDailyRate(new BigDecimal("11900"));
            cherryPicker.setWeeklyRate(new BigDecimal("76500"));
            cherryPicker.setMonthlyRate(new BigDecimal("272000"));
            cherryPicker.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            cherryPicker.setCondition(Equipment.EquipmentCondition.GOOD);
            cherryPicker.setManufacturer("JLG");
            cherryPicker.setModel("20MVL");
            cherryPicker.setYearOfManufacture(2020);
            cherryPicker.setIsActive(true);
            cherryPicker.setSecurityDeposit(new BigDecimal("51000"));
            equipmentRepository.save(cherryPicker);

            // CONCRETE EQUIPMENT
            Equipment concreteMixer = new Equipment();
            concreteMixer.setEquipmentCode("EQ000013");
            concreteMixer.setName("Concrete Mixer 10/7 CFT");
            concreteMixer.setDescription("Heavy-duty concrete mixer for on-site concrete preparation.");
            concreteMixer.setCategoryId(concreteEquipment.getId());
            concreteMixer.setLocationId(warehouseA.getId());
            concreteMixer.setDailyRate(new BigDecimal("4250"));
            concreteMixer.setWeeklyRate(new BigDecimal("25500"));
            concreteMixer.setMonthlyRate(new BigDecimal("93500"));
            concreteMixer.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            concreteMixer.setCondition(Equipment.EquipmentCondition.GOOD);
            concreteMixer.setManufacturer("Schwing Stetter");
            concreteMixer.setModel("CM 10/7");
            concreteMixer.setYearOfManufacture(2019);
            concreteMixer.setIsActive(true);
            concreteMixer.setSecurityDeposit(new BigDecimal("17000"));
            equipmentRepository.save(concreteMixer);

            Equipment concretePump = new Equipment();
            concretePump.setEquipmentCode("EQ000014");
            concretePump.setName("Concrete Pump Schwing SP1800");
            concretePump.setDescription("Trailer-mounted concrete pump for high-rise construction projects.");
            concretePump.setCategoryId(concreteEquipment.getId());
            concretePump.setLocationId(warehouseB.getId());
            concretePump.setDailyRate(new BigDecimal("34000"));
            concretePump.setWeeklyRate(new BigDecimal("212500"));
            concretePump.setMonthlyRate(new BigDecimal("765000"));
            concretePump.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            concretePump.setCondition(Equipment.EquipmentCondition.EXCELLENT);
            concretePump.setManufacturer("Schwing");
            concretePump.setModel("SP1800");
            concretePump.setYearOfManufacture(2021);
            concretePump.setIsActive(true);
            concretePump.setSecurityDeposit(new BigDecimal("153000"));
            equipmentRepository.save(concretePump);

            Equipment concreteCutter = new Equipment();
            concreteCutter.setEquipmentCode("EQ000015");
            concreteCutter.setName("Concrete Floor Saw Husqvarna FS413");
            concreteCutter.setDescription("Heavy-duty floor saw for cutting concrete, asphalt, and reinforced concrete.");
            concreteCutter.setCategoryId(concreteEquipment.getId());
            concreteCutter.setLocationId(serviceCenter.getId());
            concreteCutter.setDailyRate(new BigDecimal("6800"));
            concreteCutter.setWeeklyRate(new BigDecimal("42500"));
            concreteCutter.setMonthlyRate(new BigDecimal("153000"));
            concreteCutter.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            concreteCutter.setCondition(Equipment.EquipmentCondition.EXCELLENT);
            concreteCutter.setManufacturer("Husqvarna");
            concreteCutter.setModel("FS413");
            concreteCutter.setYearOfManufacture(2022);
            concreteCutter.setIsActive(true);
            concreteCutter.setSecurityDeposit(new BigDecimal("29750"));
            equipmentRepository.save(concreteCutter);

            // COMPACTION EQUIPMENT
            Equipment vibratorRoller = new Equipment();
            vibratorRoller.setEquipmentCode("EQ000016");
            vibratorRoller.setName("Vibratory Roller Dynapac CC1300");
            vibratorRoller.setDescription("Single drum vibratory roller for soil and asphalt compaction.");
            vibratorRoller.setCategoryId(compactionEquipment.getId());
            vibratorRoller.setLocationId(warehouseA.getId());
            vibratorRoller.setDailyRate(new BigDecimal("17000"));
            vibratorRoller.setWeeklyRate(new BigDecimal("102000"));
            vibratorRoller.setMonthlyRate(new BigDecimal("382500"));
            vibratorRoller.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            vibratorRoller.setCondition(Equipment.EquipmentCondition.GOOD);
            vibratorRoller.setManufacturer("Dynapac");
            vibratorRoller.setModel("CC1300");
            vibratorRoller.setYearOfManufacture(2020);
            vibratorRoller.setIsActive(true);
            vibratorRoller.setSecurityDeposit(new BigDecimal("76500"));
            equipmentRepository.save(vibratorRoller);

            Equipment plateCompactor = new Equipment();
            plateCompactor.setEquipmentCode("EQ000017");
            plateCompactor.setName("Plate Compactor Wacker WP1550");
            plateCompactor.setDescription("Single direction plate compactor for compacting granular soils.");
            plateCompactor.setCategoryId(compactionEquipment.getId());
            plateCompactor.setLocationId(warehouseB.getId());
            plateCompactor.setDailyRate(new BigDecimal("3400"));
            plateCompactor.setWeeklyRate(new BigDecimal("21250"));
            plateCompactor.setMonthlyRate(new BigDecimal("76500"));
            plateCompactor.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            plateCompactor.setCondition(Equipment.EquipmentCondition.EXCELLENT);
            plateCompactor.setManufacturer("Wacker Neuson");
            plateCompactor.setModel("WP1550");
            plateCompactor.setYearOfManufacture(2021);
            plateCompactor.setIsActive(true);
            plateCompactor.setSecurityDeposit(new BigDecimal("12750"));
            equipmentRepository.save(plateCompactor);

            Equipment tamperRammer = new Equipment();
            tamperRammer.setEquipmentCode("EQ000018");
            tamperRammer.setName("Tamping Rammer Mikasa MTX-70");
            tamperRammer.setDescription("Jumping jack compactor for trench and confined area compaction.");
            tamperRammer.setCategoryId(compactionEquipment.getId());
            tamperRammer.setLocationId(serviceCenter.getId());
            tamperRammer.setDailyRate(new BigDecimal("2550"));
            tamperRammer.setWeeklyRate(new BigDecimal("15300"));
            tamperRammer.setMonthlyRate(new BigDecimal("59500"));
            tamperRammer.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            tamperRammer.setCondition(Equipment.EquipmentCondition.GOOD);
            tamperRammer.setManufacturer("Mikasa");
            tamperRammer.setModel("MTX-70");
            tamperRammer.setYearOfManufacture(2020);
            tamperRammer.setIsActive(true);
            tamperRammer.setSecurityDeposit(new BigDecimal("10200"));
            equipmentRepository.save(tamperRammer);

            // POWER TOOLS & GENERATORS
            Equipment dieselGenerator = new Equipment();
            dieselGenerator.setEquipmentCode("EQ000019");
            dieselGenerator.setName("Diesel Generator 125 KVA");
            dieselGenerator.setDescription("Silent diesel generator for continuous power supply at construction sites.");
            dieselGenerator.setCategoryId(powerTools.getId());
            dieselGenerator.setLocationId(warehouseA.getId());
            dieselGenerator.setDailyRate(new BigDecimal("8500"));
            dieselGenerator.setWeeklyRate(new BigDecimal("51000"));
            dieselGenerator.setMonthlyRate(new BigDecimal("191250"));
            dieselGenerator.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            dieselGenerator.setCondition(Equipment.EquipmentCondition.EXCELLENT);
            dieselGenerator.setManufacturer("Kirloskar");
            dieselGenerator.setModel("KG1-125AS");
            dieselGenerator.setYearOfManufacture(2021);
            dieselGenerator.setIsActive(true);
            dieselGenerator.setIsFeatured(true);
            dieselGenerator.setSecurityDeposit(new BigDecimal("38250"));
            equipmentRepository.save(dieselGenerator);

            Equipment weldingMachine = new Equipment();
            weldingMachine.setEquipmentCode("EQ000020");
            weldingMachine.setName("Welding Machine 400A Miller");
            weldingMachine.setDescription("Heavy-duty MIG/TIG welding machine for industrial fabrication.");
            weldingMachine.setCategoryId(powerTools.getId());
            weldingMachine.setLocationId(warehouseB.getId());
            weldingMachine.setDailyRate(new BigDecimal("5100"));
            weldingMachine.setWeeklyRate(new BigDecimal("29750"));
            weldingMachine.setMonthlyRate(new BigDecimal("110500"));
            weldingMachine.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            weldingMachine.setCondition(Equipment.EquipmentCondition.GOOD);
            weldingMachine.setManufacturer("Miller");
            weldingMachine.setModel("Syncrowave 400");
            weldingMachine.setYearOfManufacture(2020);
            weldingMachine.setIsActive(true);
            weldingMachine.setSecurityDeposit(new BigDecimal("21250"));
            equipmentRepository.save(weldingMachine);

            Equipment airCompressor = new Equipment();
            airCompressor.setEquipmentCode("EQ000021");
            airCompressor.setName("Air Compressor 185 CFM");
            airCompressor.setDescription("Portable diesel air compressor for pneumatic tools and sandblasting.");
            airCompressor.setCategoryId(powerTools.getId());
            airCompressor.setLocationId(serviceCenter.getId());
            airCompressor.setDailyRate(new BigDecimal("6800"));
            airCompressor.setWeeklyRate(new BigDecimal("42500"));
            airCompressor.setMonthlyRate(new BigDecimal("153000"));
            airCompressor.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            airCompressor.setCondition(Equipment.EquipmentCondition.EXCELLENT);
            airCompressor.setManufacturer("Atlas Copco");
            airCompressor.setModel("XAS 185");
            airCompressor.setYearOfManufacture(2022);
            airCompressor.setIsActive(true);
            airCompressor.setSecurityDeposit(new BigDecimal("29750"));
            equipmentRepository.save(airCompressor);

            Equipment demolitionHammer = new Equipment();
            demolitionHammer.setEquipmentCode("EQ000022");
            demolitionHammer.setName("Demolition Hammer Bosch GSH 11E");
            demolitionHammer.setDescription("Electric breaker hammer for concrete demolition and chiseling work.");
            demolitionHammer.setCategoryId(powerTools.getId());
            demolitionHammer.setLocationId(warehouseB.getId());
            demolitionHammer.setDailyRate(new BigDecimal("1700"));
            demolitionHammer.setWeeklyRate(new BigDecimal("10200"));
            demolitionHammer.setMonthlyRate(new BigDecimal("38250"));
            demolitionHammer.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            demolitionHammer.setCondition(Equipment.EquipmentCondition.GOOD);
            demolitionHammer.setManufacturer("Bosch");
            demolitionHammer.setModel("GSH 11E");
            demolitionHammer.setYearOfManufacture(2021);
            demolitionHammer.setIsActive(true);
            demolitionHammer.setSecurityDeposit(new BigDecimal("8500"));
            equipmentRepository.save(demolitionHammer);

            Equipment waterPump = new Equipment();
            waterPump.setEquipmentCode("EQ000023");
            waterPump.setName("Dewatering Pump 6 inch Honda");
            waterPump.setDescription("High-capacity water pump for construction site dewatering and drainage.");
            waterPump.setCategoryId(powerTools.getId());
            waterPump.setLocationId(warehouseA.getId());
            waterPump.setDailyRate(new BigDecimal("4250"));
            waterPump.setWeeklyRate(new BigDecimal("25500"));
            waterPump.setMonthlyRate(new BigDecimal("93500"));
            waterPump.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            waterPump.setCondition(Equipment.EquipmentCondition.EXCELLENT);
            waterPump.setManufacturer("Honda");
            waterPump.setModel("WB30XT");
            waterPump.setYearOfManufacture(2021);
            waterPump.setIsActive(true);
            waterPump.setSecurityDeposit(new BigDecimal("17000"));
            equipmentRepository.save(waterPump);

            Equipment towerLight = new Equipment();
            towerLight.setEquipmentCode("EQ000024");
            towerLight.setName("Tower Light 4x1000W Diesel");
            towerLight.setDescription("Mobile lighting tower for night construction work and events.");
            towerLight.setCategoryId(powerTools.getId());
            towerLight.setLocationId(serviceCenter.getId());
            towerLight.setDailyRate(new BigDecimal("3400"));
            towerLight.setWeeklyRate(new BigDecimal("21250"));
            towerLight.setMonthlyRate(new BigDecimal("76500"));
            towerLight.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            towerLight.setCondition(Equipment.EquipmentCondition.GOOD);
            towerLight.setManufacturer("Terex");
            towerLight.setModel("AL4000");
            towerLight.setYearOfManufacture(2020);
            towerLight.setIsActive(true);
            towerLight.setSecurityDeposit(new BigDecimal("12750"));
            equipmentRepository.save(towerLight);

            log.info("Sample equipment created: 24 items");
        }

        log.info("Data initialization completed successfully!");
    }
}
