package com.university.referral.service;

import com.university.referral.model.Appointment;
import com.university.referral.model.Patient;
import com.university.referral.util.CSVDataStore;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService {
    private CSVDataStore dataStore;
    private List<Appointment> appointments;
    private PatientService patientService;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public AppointmentService() {
        dataStore = CSVDataStore.getInstance();
        appointments = new ArrayList<>();
        patientService = new PatientService();
        loadAppointmentsFromFile();
    }

    private void loadAppointmentsFromFile() {
        File file = new File("data/appointments.csv");
        if (!file.exists()) {
            System.out.println("Appointments file does not exist yet. Starting fresh.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // skip header
                }

                String[] values = line.split(",");
                if (values.length >= 13) {
                    try {
                        String appointmentId = values[0];
                        String patientId = values[1];
                        String clinicianId = values[2];
                        String facilityId = values[3];
                        LocalDate date = LocalDate.parse(values[4], DATE_FORMAT);
                        LocalTime time = LocalTime.parse(values[5], TIME_FORMAT);
                        int duration = Integer.parseInt(values[6]);
                        String type = values[7];
                        String status = values[8];
                        String reason = values[9];
                        String notes = values[10];
                        LocalDate createdDate = LocalDate.parse(values[11], DATE_FORMAT);
                        LocalDate lastModified = LocalDate.parse(values[12], DATE_FORMAT);

                        Patient patient = patientService.getPatientByID(patientId);
                        if (patient == null) {
                            System.err.println("Patient not found for appointment: " + appointmentId);
                            continue;
                        }

                        Appointment appointment = new Appointment(
                                appointmentId,
                                patientId,
                                clinicianId,
                                facilityId,
                                date,
                                time,
                                duration,
                                type,
                                reason,
                                notes
                        );

                        // Set additional info
                        appointment.setStatus(status);
                        appointments.add(appointment);

                    } catch (Exception e) {
                        System.err.println("Error parsing appointment line: " + line);
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("Loaded " + appointments.size() + " appointments from file.");

        } catch (IOException e) {
            System.err.println("Error reading appointments file: " + e.getMessage());
        }
    }

    private void updateAppointmentInFile(Appointment updatedAppointment) {
        File inputFile = new File("data/appointments.csv");
        File tempFile = new File("data/appointments_temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    writer.write(line); // header
                    writer.newLine();
                    firstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 13 && values[0].equals(updatedAppointment.getAppointmentId())) {
                    String[] updatedData = {
                            updatedAppointment.getAppointmentId(),
                            updatedAppointment.getPatientId(),
                            updatedAppointment.getClinicianId() != null ? updatedAppointment.getClinicianId() : "N/A",
                            updatedAppointment.getFacilityId(),
                            updatedAppointment.getAppointmentDate().format(DATE_FORMAT),
                            updatedAppointment.getAppointmentTime().format(TIME_FORMAT),
                            String.valueOf(updatedAppointment.getDurationMinutes()),
                            updatedAppointment.getAppointmentType(),
                            updatedAppointment.getStatus(),
                            updatedAppointment.getReasonForVisit(),
                            updatedAppointment.getNotes() != null ? updatedAppointment.getNotes() : "",
                            updatedAppointment.getCreatedDate().toLocalDate().format(DATE_FORMAT),
                            updatedAppointment.getLastModified().toLocalDate().format(DATE_FORMAT)
                    };
                    writer.write(String.join(",", updatedData));
                    writer.newLine();
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Error updating appointment in file: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        if (inputFile.delete()) {
            if (!tempFile.renameTo(inputFile)) {
                System.err.println("Could not rename temp file to original file");
            }
        } else {
            System.err.println("Could not delete original file");
        }
    }

    public boolean createAppointment(Appointment appointment) {
        appointments.add(appointment);
        return saveAppointmentToFile(appointment);
    }

    private boolean saveAppointmentToFile(Appointment appointment) {
        String[] data = {
                appointment.getAppointmentId(),
                appointment.getPatientId(),
                appointment.getClinicianId() != null ? appointment.getClinicianId() : "N/A",
                appointment.getFacilityId(),
                appointment.getAppointmentDate().format(DATE_FORMAT),
                appointment.getAppointmentTime().format(TIME_FORMAT),
                String.valueOf(appointment.getDurationMinutes()),
                appointment.getAppointmentType(),
                appointment.getStatus(),
                appointment.getReasonForVisit(),
                appointment.getNotes() != null ? appointment.getNotes() : "",
                appointment.getCreatedDate().toLocalDate().format(DATE_FORMAT),
                appointment.getLastModified().toLocalDate().format(DATE_FORMAT)
        };
        return dataStore.appendData("data/appointments.csv", data);

}

    public Appointment getAppointmentByID(String appointmentId) {
        return appointments.stream()
                .filter(a -> a.getAppointmentId().equals(appointmentId))
                .findFirst()
                .orElse(null);
    }

    public List<Appointment> getAppointmentsByPatient(String patientId) {
        List<Appointment> list = new ArrayList<>();
        for (Appointment apt : appointments) {
            if (apt.getPatientId().equals(patientId)) list.add(apt);
        }
        return list;
    }

    public List<Appointment> getAppointmentsByClinician(String clinicianId) {
        List<Appointment> list = new ArrayList<>();
        for (Appointment apt : appointments) {
            if (apt.getClinicianId() != null && apt.getClinicianId().equals(clinicianId)) list.add(apt);
        }
        return list;
    }

    public boolean cancelAppointment(String appointmentId) {
        Appointment apt = getAppointmentByID(appointmentId);
        if (apt != null) {
            apt.cancel();
            updateAppointmentInFile(apt);
            return true;
        }
        return false;
    }

    public boolean rescheduleAppointment(Appointment appointment) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getPatientId().equals(appointment.getPatientId())) {
                appointments.set(i, appointment);
                updateAppointmentInFile(appointment);
                return true;
            }
        }
        return false;
    }

    public String generateAppointmentID() {
        return "APT" + System.currentTimeMillis();
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }
}
