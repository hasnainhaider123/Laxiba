package com.grtteam.laxiba.util;

import com.grtteam.laxiba.api.API;

import static com.grtteam.laxiba.api.API.*;

/**
 * Created by oleh on 02.08.16.
 */
public class LanguageCodesHelper {


    public static final String LANG_ENGLISH = "en";
    public static final String LANG_GERMAN = "de";
    public static final String LANG_SPANISH = "es";
    public static final String LANG_FRANCH = "fr";
    public static final String LANG_ITALIAN = "it";

    private LanguageCodesHelper() {
    }

    /**
     * Converts language code from api format(e, d, s, f, i)
     * to android locale format (en, de, es, fr, it)
     *
     * @param apiLang - language code in format [e, d, s, f, i]
     * @return - converted code in format [en, de, es, fr, it]
     */
    public static String apiToAndroid(String apiLang) {
        if (apiLang == null) {
            return null;
        }
        switch (apiLang) {
            case API_LANG_ENGLISH:
                return LANG_ENGLISH;
            case API_LANG_GERMAN:
                return LANG_GERMAN;
            case API_LANG_SPANISH:
                return LANG_SPANISH;
            case API_LANG_FRANCH:
                return LANG_FRANCH;
            case API_LANG_ITALIAN:
                return LANG_ITALIAN;
            default:
                return null;
        }
    }

    /**
     * Converts language code from android locale format (en, de, es, fr, it)
     * to api format(e, d, s, f, i)
     *
     * @param androidLang - language code in format [en, de, es, fr, it]
     * @return - converted code in format [e, d, s, f, i]
     */
    public static String androidToApi(String androidLang) {
        switch (androidLang) {
            case LANG_ENGLISH:
                return API_LANG_ENGLISH;
            case LANG_GERMAN:
                return API_LANG_GERMAN;
            case LANG_SPANISH:
                return API_LANG_SPANISH;
            case LANG_FRANCH:
                return API_LANG_FRANCH;
            case LANG_ITALIAN:
                return API_LANG_ITALIAN;
            default:
                return API.API_LANG_ENGLISH;
        }

    }


}
