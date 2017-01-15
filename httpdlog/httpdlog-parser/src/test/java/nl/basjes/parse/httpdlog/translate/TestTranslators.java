/*
 * Apache HTTPD & NGINX Access log parsing made easy
 * Copyright (C) 2011-2017 Niels Basjes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.basjes.parse.httpdlog.translate;

import nl.basjes.parse.core.nl.basjes.parse.core.test.DissectorTester;
import nl.basjes.parse.httpdlog.dissectors.RequestCookieListDissector;
import nl.basjes.parse.httpdlog.dissectors.ResponseSetCookieDissector;
import nl.basjes.parse.httpdlog.dissectors.ResponseSetCookieListDissector;
import nl.basjes.parse.httpdlog.dissectors.translate.ConvertCLFIntoNumber;
import nl.basjes.parse.httpdlog.dissectors.translate.ConvertNumberIntoCLF;
import org.junit.Test;

public class TestTranslators {

    @Test
    public void testCLFToNumberMin() throws Exception {
        DissectorTester.create()
            .withDissectorUnderDummyRoot("root", new ConvertCLFIntoNumber("IN","OUT"))
            .withInput(null) // A '-' in the input file goes into the dissector as a null value
            .expect("OUT:root", 0L)
            .checkExpectations();
    }

    @Test
    public void testCLFToNumber0() throws Exception {
        DissectorTester.create()
            .withDissectorUnderDummyRoot("root", new ConvertCLFIntoNumber("IN","OUT"))
            .withInput("0")
            .expect("OUT:root", 0L)
            .checkExpectations();
    }

    @Test
    public void testCLFToNumber1() throws Exception {
        DissectorTester.create()
            .withDissectorUnderDummyRoot("root", new ConvertCLFIntoNumber("IN","OUT"))
            .withInput("1")
            .expect("OUT:root", 1L)
            .checkExpectations();
    }

    @Test
    public void testNumberToCLF0() throws Exception {
        DissectorTester.create()
            .withDissectorUnderDummyRoot("root", new ConvertNumberIntoCLF("IN","OUT"))
            .withInput("0")
            .expect("OUT:root", (String)null)
            .checkExpectations();
    }

    @Test
    public void testNumberToCLF1() throws Exception {
        DissectorTester.create()
            .withDissectorUnderDummyRoot("root", new ConvertNumberIntoCLF("IN","OUT"))
            .withInput("1")
            .expect("OUT:root", 1L)
            .checkExpectations();
    }


}
