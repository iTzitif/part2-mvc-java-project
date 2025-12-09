# University Referral Management System
A Java-based MVC desktop application developed using **Swing**, **CSV-based persistence**, and a modular architecture.  
This system manages Patients, Appointments, Clinicians, Prescriptions, Referrals, Staff Members, and Medical Facilities.

The project follows strict **Model–View–Controller (MVC)** principles and uses a **centralized Singleton ApplicationDataStore** for loading and saving data.

---

## 📌 Features

### ✅ Patient Management
- Add, edit, delete patient records  
- Search patients by ID or name  
- Persistent storage using CSV  
- Clean table UI with sorting and filtering  

### ✅ Appointment Management
- Create, update, delete appointments  
- Link appointments with patients and clinicians  
- CSV-based appointment storage  
- Full CRUD support via dialogs  

### ✅ Clinician Management
- Maintain clinician profiles (ID, name, specialty, availability)  
- Search and filter capabilities  
- CSV integration  

### ✅ Prescription Management
- Manage prescription entries  
- Create prescriptions linked to patients and appointments  
- Auto-save to CSV  

### ✅ Referral Request Module (Core Requirement)
- Generate referral requests  
- Stores requests in CSV  
- Writes formatted referral output to a text file  
- ReferralRequestService (Singleton) handles output generation  

### ✅ Staff & Facility Management
- CRUD operations for staff members  
- CRUD operations for medical facilities  
- CSV persistence for both  

### ✅ Centralized DataStore (Singleton)
- Loads all CSV modules at startup  
- Provides shared data access across all controllers  
- Ensures consistency throughout the application  

### ✅ Utility Layer
- CsvFileReader: loads CSV with graceful fallback  
- CsvFileWriter: overwrites CSV safely using string rows  

---

## 🏗️ Project Structure (MVC)

