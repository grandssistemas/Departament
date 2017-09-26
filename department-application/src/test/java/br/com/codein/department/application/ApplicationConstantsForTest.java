package br.com.codein.department.application;

/**
 * Created by rafael on 06/09/16.
 */
import io.gumga.core.GumgaValues;

public class ApplicationConstantsForTest implements GumgaValues {

    @Override
    public String getGumgaSecurityUrl() {
        return "http://192.168.25.250/security-api/publicoperation";
    }

    @Override
    public boolean isLogActive() {
        return true;
    }
}