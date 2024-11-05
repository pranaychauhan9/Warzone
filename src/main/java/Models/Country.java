package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This model class manages all the countries on the map.
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
public class Country implements Serializable {

	/**
	 * Army count.
	 */
	Integer d_armyCount;

	/**
	 * country ID.
	 */
	Integer d_countryId;

	/**
	 * continent ID.
	 */
	Integer d_continentId;

	/**
	 * country name.
	 */
	String d_countryName;

	/**
	 * list of the countries which are adjacent to the existing one.
	 */
	List<Integer> d_adjacentCountryIds = new ArrayList<Integer>();;

	/**
	 * constructor of this class.
	 *
	 * @param p_countryId   country ID
	 * @param p_countryName country name
	 * @param p_continentId continent ID
	 */
	public Country(int p_countryId, String p_countryName, int p_continentId) {
		d_countryId = p_countryId;
		d_countryName = p_countryName;
		d_continentId = p_continentId;
	}

	/**
	 * constructor of this class.
	 *
	 * @param p_countryId   country ID
	 * @param p_continentId continent ID
	 */
	public Country(int p_countryId, int p_continentId) {
		d_countryId = p_countryId;
		d_continentId = p_continentId;
	}

	/**
	 * single parameter constructor.
	 * 
	 * @param p_countryName name of the country
	 */
	public Country(String p_countryName) {
		d_countryName = p_countryName;
	}

	/**
	 * Get the armies count.
	 *
	 * @return armies
	 */
	public Integer getD_armyCount() {
		if (d_armyCount == null) {
			return 0;
		}
		return d_armyCount;
	}

	/**
	 * Set the army count.
	 *
	 * @param p_armyCount armies
	 */
	public void setD_armyCount(Integer p_armyCount) {
		this.d_armyCount = p_armyCount;
	}

	/**
	 * establish a getter function to obtain the country ID.
	 *
	 * @return country ID
	 */
	public Integer getD_countryId() {
		return d_countryId;
	}

	/**
	 * establish a setter method to set the country ID.
	 *
	 * @param p_countryId country ID
	 */
	public void setD_countryId(Integer p_countryId) {
		this.d_countryId = p_countryId;
	}

	/**
	 * establish a getter method to get the continent ID.
	 *
	 * @return continent ID
	 */
	public Integer getD_continentId() {
		return d_continentId;
	}

	/**
	 * establish a setter method to set the continent ID.
	 *
	 * @param p_continentId continent ID
	 */
	public void setD_continentId(Integer p_continentId) {
		this.d_continentId = p_continentId;
	}

	/**
	 * establish a getter method to get the adjacent country IDs.
	 *
	 * @return list of adjacent country IDs
	 */
	public List<Integer> getD_adjacentCountryIds() {
		if (d_adjacentCountryIds == null) {
			d_adjacentCountryIds = new ArrayList<Integer>();
		}

		return d_adjacentCountryIds;
	}

	/**
	 * establish a setter method to set the adjacent country IDs.
	 *
	 * @param p_adjacentCountryIds list of adjacent country IDs
	 */
	public void setD_adjacentCountryIds(List<Integer> p_adjacentCountryIds) {
		this.d_adjacentCountryIds = p_adjacentCountryIds;
	}

	/**
	 * establish a getter method to get the name of the country.
	 *
	 * @return name of the country
	 */
	public String getD_countryName() {
		return d_countryName;
	}

	/**
	 * establish a setter method to set the name of the country.
	 *
	 * @param p_countryName name of the country
	 */
	public void setD_countryName(String p_countryName) {
		this.d_countryName = p_countryName;
	}

	/**
	 * Append the country ID to the list of neighbors.
	 * 
	 * @param p_countryNeighbourId Id of country to be added
	 */
	public void addNeighbour(Integer p_countryNeighbourId) {
		if (!d_adjacentCountryIds.contains(p_countryNeighbourId))
			d_adjacentCountryIds.add(p_countryNeighbourId);
	}

	/**
	 * Delete the country ID from the neighbor list.
	 * 
	 * @param p_countryNeighbourId Id of country to be removed
	 */
	public void removeNeighbour(Integer p_countryNeighbourId) {
		if (d_adjacentCountryIds.contains(p_countryNeighbourId)) {
			d_adjacentCountryIds.remove(d_adjacentCountryIds.indexOf(p_countryNeighbourId));
		} else {
			System.out.println("No Such Neighbour Exists");
		}
	}
}
