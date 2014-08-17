/*
 * Apache HTTPD logparsing made easy
 * Copyright (C) 2013 Niels Basjes
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package nl.basjes.parse.http.disectors;

import nl.basjes.parse.core.Castable;
import nl.basjes.parse.core.Disector;
import nl.basjes.parse.core.Parsable;
import nl.basjes.parse.core.ParsedField;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ResponseSetCookieDisector extends Disector {
    // --------------------------------------------

    private static final String INPUT_TYPE = "HTTP.SETCOOKIE";

    @Override
    public String getInputType() {
        return INPUT_TYPE;
    }

    // --------------------------------------------

    /** This should output all possible types */
    @Override
    public List<String> getPossibleOutput() {
        List<String> result = new ArrayList<String>();
        result.add("STRING:value");
        result.add("STRING:expires");
        result.add("STRING:path");
        result.add("STRING:domain");
        return result;
    }

    // --------------------------------------------

    @Override
    protected void initializeNewInstance(Disector newInstance) {
        // Nothing to do
    }


    @Override
    public void prepareForDisect(final String inputname, final String outputname) {
        // We do not do anything extra here
    }

    // --------------------------------------------

    @Override
    public void prepareForRun() {
        // We do not do anything extra here
    }

    // --------------------------------------------

    @Override
    public void disect(final Parsable<?> parsable, final String inputname) {
        final ParsedField field = parsable.getParsableField(INPUT_TYPE, inputname);

        final String fieldValue = field.getValue();
        if (fieldValue == null || fieldValue.isEmpty()){
            return; // Nothing to do here
        }

        Long nowSeconds = System.currentTimeMillis()/1000;
        List<HttpCookie> cookies = HttpCookie.parse(fieldValue);

        for (HttpCookie cookie : cookies) {
            parsable.addDisection(inputname, getDisectionType(inputname, "value"),   "value",   cookie.getValue(), EnumSet.of(Castable.STRING));
            parsable.addDisection(inputname, getDisectionType(inputname, "expires"), "expires",
                    Long.toString(nowSeconds+cookie.getMaxAge()), EnumSet.of(Castable.STRING));
            parsable.addDisection(inputname, getDisectionType(inputname, "path"),    "path",    cookie.getPath(), EnumSet.of(Castable.STRING));
            parsable.addDisection(inputname, getDisectionType(inputname, "domain"),  "domain",  cookie.getDomain(), EnumSet.of(Castable.STRING));
            parsable.addDisection(inputname, getDisectionType(inputname, "comment"), "comment", cookie.getComment(), EnumSet.of(Castable.STRING));
        }
    }

    // --------------------------------------------

    /**
     * This determines the type of the value that was just found.
     * This method is intended to be overruled by a subclass
     */
    public String getDisectionType(final String basename, final String name) {
        return "STRING"; // Possible outputs are of the same type.
    }

    // --------------------------------------------

}
