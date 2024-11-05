package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import CommonFunctions.CommonCode;

/**
 * This model class manages all the Continents in the map.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 * 
 */
public class Continent implements Serializable {

	/**
	 * continent ID.
	 */
	Integer d_continentID;

	/**
	 * continent name.
	 */
	String d_continentName;

	/**
	 * continent value.
	 */
	Integer d_continentValue;

	/**
	 * List of countries.
	 */
	List<Country> d_countries;

	/**
	 * constructor to this class.
	 *
	 * @param p_continentID    continent ID
	 * @param p_continentName  continent name
	 * @param p_continentValue continent value
	 */
	public Continent(Integer p_continentID, String p_continentName, int p_continentValue) {
		this.d_continentID = p_continentID;
		this.d_continentName = p_continentName;
		this.d_continentValue = p_continentValue;
	}

	/**
	 * default contructor to the class.
	 */
	public Continent() {

	}

	/**
	 * constructor to this class.
	 *
	 * @param p_continentName continent name
	 */
	public Continent(String p_continentName) {
		this.d_continentName = p_continentName;
	}

	/**
	 * establish a getter method to get the continent ID.
	 *
	 * @return continent ID
	 */
	public Integer getD_continentID() {
		return d_continentID;
	}

	/**
	 * establish a setter method to set the continenet ID.
	 *
	 * @param p_continentID continent ID
	 */
	public void setD_continentID(Integer p_continentID) {
		this.d_continentID = p_continentID;
	}

	/**
	 * establish a getter method to get the continent name.
	 *
	 * @return continent name
	 */
	public String getD_continentName() {
		return d_continentName;
	}

	/**
	 * establish a setter method to set the continent name.
	 *
	 * @param p_continentName name of the continent
	 */
	public void setD_continentName(String p_continentName) {
		this.d_continentName = p_continentName;
	}

	/**
	 * establish a getter method to get the continent value.
	 *
	 * @return continent value
	 */
	public Integer getD_continentValue() {
		return d_continentValue;
	}

	/**
	 * establish a setter method to set the continent value.
	 *
	 * @param p_continentValue the continent value
	 */
	public void setD_continentValue(Integer p_continentValue) {
		this.d_continentValue = p_continentValue;
	}

	/**
	 * establish a getter method to get the countries.
	 *
	 * @return list of countries
	 */
	public List<Country> getD_countries() {
		return d_countries;
	}

	/**
	 * establish a setter method to set the countries.
	 *
	 * @param p_countries list of countries
	 */
	public void setD_countries(List<Country> p_countries) {
		this.d_countries = p_countries;
	}

	/**
	 * adds the specified country.
	 *
	 * @param p_country the country to be added
	 */
	public void addCountry(Country p_country) {
		if (d_countries != null) {
			d_countries.add(p_country);
		} else {
			d_countries = new ArrayList<Country>();
			d_countries.add(p_country);
		}
	}

	/**
	 * removes target Country from Continent.
	 * 
	 * @param p_targetCountryId country to be removed
	 */
	public void removeCountry(Country p_targetCountryId) {
		if (d_countries == null) {
			System.out.println("No such Country Exists");
		} else {
			d_countries.remove(p_targetCountryId);
		}
	}

	/**
	 * Removes particular country ID from the neighbor list of all countries in
	 * continent.
	 * Removes particular country ID from the neighbor list of all countries in
	 * continent.
	 * 
	 * @param p_targetCountryId ID of country to be removed
	 */
	public void removeCountryNeighboursFromAll(Integer p_targetCountryId) {
		if (null != d_countries && !d_countries.isEmpty()) {
			for (Country c : d_countries) {
				if (!CommonCode.isNull(c.d_adjacentCountryIds)) {
					if (c.getD_adjacentCountryIds().contains(p_targetCountryId)) {
						c.removeNeighbour(p_targetCountryId);
					}
				}
			}
		}
	}

}
