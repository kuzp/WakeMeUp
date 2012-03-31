import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.*;
import org.htmlparser.util.NodeList;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Bogdanov Kirill
 * Date: 21.02.12
 * Time: 19:32
 */
public class HTMLParser {
    public static void main(String[] args) throws Exception {
        //parse("2742");
        //new Schedule().createSchedule("2244");
        //saveSchedule();
        //System.out.println(new Schedule().getRings("2742", "�����", "���"));
        //System.out.println(new Schedule().getSchedule("2742", "�����", "���"));
        //new SettingsManager().createSettings(1, "2742");
        new SettingsManager().updateSettings(1, "�����", "���", "09:30", "no", "07:30");
        new SettingsManager().updateSettings(1, "�����", "���", "11:00", "yes", "11:00");
        System.out.println(new SettingsManager().getRings(1, "�����", "���"));
    }

    public static void saveSchedule() throws Exception {
        Parser parser = new Parser("http://www.ifmo.ru/schedule/lessons.htm");
        NodeList nl = parser.parse(new AndFilter(new HasParentFilter(new AndFilter(new TagNameFilter("a"),
                new HasAttributeFilter("href"))), new HasParentFilter(new HasParentFilter(
                new HasAttributeFilter("valign", "top")))));
        Node[] nodes = nl.toNodeArray();
        Schedule schedule = new Schedule();
        for (Node node : nodes) {
            String temp = node.getText();
            schedule.createSchedule(temp);
            parse(temp);
        }
    }

    public static ArrayList<String> parse(String group) throws Exception {
        Parser parser = new Parser("http://www.ifmo.ru/schedule_search/1/" + group + "/" + group + ".htm");
        NodeFilter[] nf = new NodeFilter[]{new HasParentFilter(new HasParentFilter(new HasParentFilter(new AndFilter(
                new TagNameFilter("table"), new HasAttributeFilter("border", "0"))))), new NotFilter(
                new TagNameFilter("strong")), new NotFilter(new TagNameFilter("small")), new NotFilter(
                new TagNameFilter("br"))};
        NodeList nl = parser.parse(new AndFilter(nf));
        Node[] nodes = nl.toNodeArray();
        ArrayList<String> al = new ArrayList<String>();
        for (Node node : nodes) {
            String temp = node.getText();
            if (temp.contains("href")) {
                al.add(node.getFirstChild().getText());
            }
            if (!temp.equals("&nbsp;") && !temp.equals("�����") && !temp.equals("������") && !temp.equals("�����")
                    && !temp.equals("�������") && !temp.equals("�������������") && !temp.contains("href")) {
                al.add(node.getText());
            }
        }
        final String[] week = new String[]{"�����������", "�������", "�����", "�������", "�������", "�������"};
        int dayN = 0;
        String day = al.get(0);
        String parity = "";
        String startTime = "";
        String endTime = "";
        String place = "";
        String subject = "";
        String type = "";
        String teacher = "";
        Schedule schedule = new Schedule();
        for (int i = 1; i < al.size(); i++) {
            if (al.get(i).trim().isEmpty()) {
                continue;
            }
            Pattern pattern = Pattern.compile("^\\d\\d:\\d\\d$");
            Matcher matcher = pattern.matcher(al.get(i));
            if (matcher.matches()) {
                startTime = al.get(i);
                endTime = al.get(++i);
                continue;
            }
            pattern = Pattern.compile("^[�-�][�-�][�-�]$");
            matcher = pattern.matcher(al.get(i));
            if (matcher.matches()) {
                parity = al.get(i);
                continue;
            }
            pattern = Pattern.compile("^(.+�\\.\\s*[0-9]+.+)");
            matcher = pattern.matcher(al.get(i));
            if (matcher.matches()) {
                place = matcher.group(1);
                continue;
            }
            pattern = Pattern.compile("([�-��-�,*\\s\\-[0-9]]+(\\(.+\\))*)\\((.+)\\).*");
            matcher = pattern.matcher(al.get(i));
            if (matcher.matches()) {
                subject = matcher.group(1);
                type = matcher.group(3);
                continue;
            }
            pattern = Pattern.compile("[�-��-�]+\\s[�-��-�]+\\s[�-��-�]+");
            matcher = pattern.matcher(al.get(i));
            if (matcher.matches()) {
                teacher = al.get(i);
            }
            if (!subject.isEmpty()) {
                schedule.saveRing(day, startTime, endTime, parity, place, subject, type, teacher, group);
            }
            parity = "";
            startTime = "";
            endTime = "";
            place = "";
            subject = "";
            type = "";
            teacher = "";
            for (int j = dayN + 1; j < week.length; j++) {
                if (al.get(i).equals(week[j])) {
                    day = week[j];
                    dayN = j;
                }
            }
        }
        return al;
    }
}
