package com.enginemachining.api.energy;

public interface IEnergyHandler {
    /**
     *
     * @param power The amount of power to be inserted
     * @param simulate Should this power be inserted or only calculate the maximum amount to be inserted
     * @return The amount of power which can be inserted
     **/
    float insertPower(float power, boolean simulate);

    /**
     *
     * @param power The amount of power to be extracted
     * @param simulate Should this power be extracted or only calculate the maximum amount to be extracted
     * @return The amount of power which can be extracted
     */
    float extractPower(float power, boolean simulate);

    /**
     * Get the amount of power stored.
     * @return Amount of power stored.
     */
    float getStoredPower();

    /**
     * Get the maximum amount of power which can be stored.
     * @return Get the maximum amount of power that can be stored.
     */
    float getMaxPower();
}
