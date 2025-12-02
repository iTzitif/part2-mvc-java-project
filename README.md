# part2-mvc-java-project
# Healthcare Appointment and Referral Management System(Java MVC).

It is a desktop Java application that will be used to handle patients, clinicians, appointments, prescription, and referral records in a small healthcare workflow.  
The system is developed on Java and Swing with the architectural pattern of Model-View- Controller (MVC).

Features

Data Management
Import the old data on CSV files.
Show Patients, Clinicians, Appointments, Staff, Prescriptions and Facilities.
Create, update and remove records using GUI.
Auto-create new referrals and prescriptions.

Referral Management (Singleton)
There is a special ReferralManager class which employs the Singleton design pattern to:
Create new referral records
Have a managed referral queue.
Export export referral summaries to a text file.

CSV File Processing
Reads the first data on the CSV files in the data/ directory.
Writes revised prescription and referral information to output files:
  - prescriptionsout.csv or .txt
  - referralsout.txt

GUI (Swing)
Panera has a clean desktop interface which was developed with:
JFrame
JPanel
JTabbedPane
JTable
JButton
JTextField

