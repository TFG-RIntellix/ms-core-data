package es.NTTEnterprise.RIntellix.ms_core_data.domain.enums;

/**
 * Enumeration representing risk grade classifications assigned by the scoring
 * model.
 * Grades range from A (lowest risk) to F (highest risk) following standard
 * credit rating conventions.
 * 
 * @author Lucía Fernández Mancebo
 * @date 21/03/2026
 */
public enum RiskGrade {

    /**
     * Grade A - Lowest risk, excellent creditworthiness.
     */
    A,

    /**
     * Grade B - Low to moderate risk, good creditworthiness.
     */
    B,

    /**
     * Grade C - Moderate risk, acceptable creditworthiness.
     */
    C,

    /**
     * Grade D - Moderately high risk, poor creditworthiness.
     */
    D,

    /**
     * Grade E - High risk, weak creditworthiness.
     */
    E,

    /**
     * Grade F - Highest risk, very poor or unacceptable creditworthiness.
     */
    F,

    /**
     * Grade G - Very high risk, extremely poor creditworthiness.
     */
    G,

    /**
     * Grade H - Extremely high risk, extremely poor creditworthiness.
     */
    H
}
