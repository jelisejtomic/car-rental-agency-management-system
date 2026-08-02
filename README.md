# Car Rental Agency Management System

A Java console application developed for managing car rental agency operations, focusing on user authentication, vehicle fleet tracking, client registrations, and rental processing. The application features console-based interactive menus, domain separation, custom mathematical rental pricing models, and CSV file persistence.

## Project Overview

This application was developed as an academic project for the _Object-Oriented Programming 1_ course. It provides a comprehensive console interface to streamline car rental operations across two primary domains:

### 1. User Domain

- **User** `Korisnik`: Abstract base entity for all system users.
    - _Attributes_: Username `korisnickoIme`, Password `lozinka`, First Name `ime`, Last Name `prezime`.
- **Agent** `Agent`: Extends `User`.
    - _Additional Attributes_: Associated Dealership `autoKuca`.
    - _Features_: Client registration, vehicle management (add, delete), rental request approval, and vehicle returns processing.
- **Client** `Klijent`: Extends `User`.
    - _Additional Attributes_: Identification Code `idKlijenta`, Rental History `zakupiVozila`.
    - _Features_: Interactive vehicle rental requesting, filtering fleet by fuel/body type, and active rental extension.

### 2. Vehicle & Rental Domain

- **Vehicle** `Vozilo`: Abstract base entity for all fleet inventory.
    - _Attributes_: Brand `marka`, Model `model`, Chassis Number `brojSasije`, Kilometers Driven `predjeniKilometri`, Fuel Type `gorivo`.
- **Passenger Vehicle** `PutnickoVozilo`: Extends `Vehicle`.
    - _Additional Attributes_: Associated Dealership `autoKuca`, Seating Capacity `brojSedista`, Body Style `karoserija`.
- **Freight Vehicle** `TeretnoVozilo`: Extends `Vehicle`.
    - _Additional Attributes_: Payload Capacity in Tons `kapacitet`.
- **Rental Agreement** `ZakupVozila`:
    - _Attributes_: Client `klijent`, Vehicle `vozilo`, Start Date `odKad`, End Date `doKad`, Time Unit `jedinicaVremena`, Issuing Agent `agentIzdao`, Receiving Agent `agentPrimio`, Estimated Cost `procenjenaVrednost`, Insurance `osiguranje`.
    - _Features_: Connects client, vehicle, handling agents, time duration, insurance, and calculated cost.
- **Insurance** `Osiguranje`: Defines coverage limit `novcaniLimit`.
- **Enumerations & Interfaces**: Fuel types `Gorivo`, Body styles `Karoserija`, Interface defining core rental operations `Unajmljivo`, Serialization/deserialization interface for converting entity objects to and from CSV format `PodaciCSV`.

Data persistence is implemented using flat-file CSV storage located inside the `data/` directory. The persistence layer adheres strictly to the following rules based on the project specification.

## Getting Started

### Prerequisites

- **Java Development Kit (JDK):** Version 11 or higher.
- **Eclipse IDE** (recommended) or any compatible Java IDE (IntelliJ IDEA, NetBeans, VS Code).

### Running via Command Line

1. **Clone the Repository**

    ```bash
    git clone https://github.com/jelisejtomic/car-rental-agency-management-system
    cd CarRentalAgencyManagementSystem
    ```

2. **Compile the Package**

    ```bash
    javac -d bin -sourcepath src src/aplikacija/Aplikacija.java
    ```

3. **Run the Application**
    ```bash
    java -cp bin aplikacija.Aplikacija
    ```

### Running via Eclipse IDE

1. Open Eclipse and select **File -> Import -> Existing Projects into Workspace**.
2. Select the `CarRentalAgencyManagementSystem` root folder.
3. Locate `src/aplikacija/Aplikacija.java`, right-click and select **Run As -> Java Application**.
