package pomalowane.sms;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import pomalowane.appointment.Appointment;
import pomalowane.client.Client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@AllArgsConstructor
@Service
public class SmsService {

    private SmsDao smsDao;

    private final String apiLogin = "webapi_merios";
    private final String apiPassword = "4069ce21";


    public void setSmsReminder(Appointment appointment) throws Exception {
        List<Sms> smsReminders = new ArrayList<>();
        // Check if day before isn't before now, if so then send sms 2hrs before
        if (appointment.getStartDate().minusHours(24).isBefore(LocalDateTime.now())) {
            String twoHoursBefore = getStringDateTwoHoursBefore(appointment);
            Sms smsTwoHoursBefore = buildSms(appointment, twoHoursBefore);
            smsDao.save(smsTwoHoursBefore);
            smsReminders.add(smsTwoHoursBefore);
        } else {
            String dayBefore = getStringDateDayBefore(appointment);
            Sms smsDayBefore = buildSms(appointment,  dayBefore);
            smsDao.save(smsDayBefore);
            smsReminders.add(smsDayBefore);

            String twoHoursBefore = getStringDateTwoHoursBefore(appointment);
            Sms smsTwoHoursBefore = buildSms(appointment, twoHoursBefore);
            smsDao.save(smsTwoHoursBefore);
            smsReminders.add(smsTwoHoursBefore);

            appointment.setSmsReminders(smsReminders);
        }
    }

    public void updateSmsReminder(Appointment appointment) throws Exception {
        SerwerSMS SerwerSMSApi = new SerwerSMS(apiLogin, apiPassword);
        List<Sms> smses = smsDao.findByAppointmentId(appointment.getId());
        for (Sms sms : smses) {
            SerwerSMSApi.message.delete(sms.getProvidedId(), null);
            smsDao.delete(sms);
        }
        setSmsReminder(appointment);
    }

    private Sms buildSms(Appointment appointment, String date) throws Exception {
        System.out.println(date);
        String providedId = sendSms(appointment, appointment.getClient(), date);

        date = date.replace("-", "/");
        date = date.replace("T", " ");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime sendDate = LocalDateTime.parse(date, formatter);
        return Sms.builder()
                .providedId(providedId)
                .sendDate(sendDate)
                .appointment(appointment)
                .build();
    }

    private String sendSms(Appointment appointment, Client client, String sendDate) throws Exception {
        SerwerSMS SerwerSMSApi = new SerwerSMS(apiLogin, apiPassword);
        String type = "json";
        SerwerSMSApi.setFormat(type);

        HashMap<String, String> options = new HashMap<>();
        options.put("test", "false");
        options.put("details", "true");
        options.put("date", sendDate);

        String formattedDate = formatDate(appointment.getStartDate());

        String text = "Gabinet NO I PIEKNIE przypomina o wizycie umówionej na dzień " + formattedDate + ". \n" +
                "Serdecznie zapraszamy. \n Jesli nie mozesz nas odwiedzic poinformuj o tym minimum 48h przed wizytą.";
        String result = SerwerSMSApi.message.sendSms(client.getPhoneNumber(), text, "Pomalowane", options);
        System.out.println(result);

        return extractSmsId(result);
    }

    private String getStringDateDayBefore(Appointment appointment) {
        return appointment.getStartDate().minusHours(24).toString();
    }

    private String getStringDateTwoHoursBefore(Appointment appointment) {
        return appointment.getStartDate().minusHours(2).toString();
    }

    private String extractSmsId(String result) {
        //System.out.println(result);
        JSONObject json = new JSONObject(result);
        JSONArray jsonArray = new JSONArray(json.get("items").toString());
        JSONObject tmp = new JSONObject(jsonArray.get(0).toString());

        return tmp.get("id").toString();
    }

    private String formatDate(LocalDateTime date) {
        return date.toString().replace("T", " o godzinie ");
    }

}

