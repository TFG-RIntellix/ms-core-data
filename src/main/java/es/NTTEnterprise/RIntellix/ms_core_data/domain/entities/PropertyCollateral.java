package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

/**
 * This class represents the collateral of a property that can be used to secure a mortgage.
 * It contains the value of the property and whether it is a first home or not.
 * @author Lucía Fernández Mancebo
 * @Date 02-28-2026
 */
public class PropertyCollateral {

    private Money propertyValue;
    private boolean isFirstHome;

    /**
     * Constructor of the PropertyCollateral class.
     * @param propertyValue the value of the property, which is a Money object that contains the amount and the currency.
     * @param isFirstHome whether the property is a first home or not, which can affect the interest rate of the mortgage.
     */
    public PropertyCollateral(Money propertyValue, boolean isFirstHome) {
        this.propertyValue = propertyValue;
        this.isFirstHome = isFirstHome;
    }

    // Getters and setters

    public Money getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Money propertyValue) {
        this.propertyValue = propertyValue;
    }

    public boolean isFirstHome() {
        return isFirstHome;
    }

    public void setFirstHome(boolean isFirstHome) {
        this.isFirstHome = isFirstHome;
    }


}
