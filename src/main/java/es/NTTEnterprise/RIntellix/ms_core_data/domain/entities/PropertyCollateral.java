package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

/**
 * This class represents the collateral of a property that can be used to secure
 * a mortgage.
 * It contains the value of the property and whether it is a first home or not.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 02-28-2026
 */
public class PropertyCollateral {

    private Money propertyValue;
    private boolean isFirstHome;

    /**
     * Constructor of the PropertyCollateral class.
     * 
     * @param propertyValue the value of the property, which is a Money object that
     *                      contains the amount and the currency.
     * @param isFirstHome   whether the property is a first home or not, which can
     *                      affect the interest rate of the mortgage.
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

    // toString, hashCode and equals
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PropertyCollateral{");
        sb.append("propertyValue=").append(propertyValue);
        sb.append(", isFirstHome=").append(isFirstHome);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((propertyValue == null) ? 0 : propertyValue.hashCode());
        result = prime * result + (isFirstHome ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PropertyCollateral other = (PropertyCollateral) obj;
        if (propertyValue == null) {
            if (other.propertyValue != null)
                return false;
        } else if (!propertyValue.equals(other.propertyValue))
            return false;
        if (isFirstHome != other.isFirstHome)
            return false;
        return true;
    }

}
