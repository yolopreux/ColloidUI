/**
 *  Colloid project
 *
 *  Combat log analizer.
 *
 *  copyright: (c) 2013 by Darek <netmik12 [AT] gmail [DOT] com>
 *  license: BSD, see LICENSE for more details
 */
package colloid.model.event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CombatThreatEvent extends CombatEvent {

    private static final long serialVersionUID = 3113199908870845807L;
    static final Pattern pattern = Pattern.compile(".*\\<(\\d+)(.*)");

    public CombatThreatEvent(Object source, String logdata) {
        super(source, logdata);
    }

    public static double parseValue(String logdata) {
        double result = 0;
        String[] data = logdata.substring(1).split("\\]\\s\\[");
        try {
            Matcher matcher = pattern.matcher(data[4].trim());
            if (matcher.matches()) {
                String firstGroup = matcher.group(1);
                result = Double.parseDouble(firstGroup);
            }
        } catch (NumberFormatException ex) {
            System.out.println(ex);
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }

        return result;
    }

    @Override
    public double getValue() {
        if (Double.isNaN(value) || value == 0) {
            value = parseValue(logdata);
        }
        return value;
    }

}
