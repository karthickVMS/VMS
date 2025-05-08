package com.akt.vms.mapper;

import org.springframework.stereotype.Component;

import com.akt.vms.dto.AddressDTO;
import com.akt.vms.dto.DriverDTO;
import com.akt.vms.entity.Address;
import com.akt.vms.entity.Driver;

@Component
public class DriverMapper {

	/**
	 * Converts a Driver entity object to a DriverDTO.
	 * 
	 * @param driver - the Driver entity to convert
	 * @return DriverDTO - the converted DTO object
	 */
	public DriverDTO toDriverDTO(Driver driver) {
		if (driver == null) {
			return null;
		}

		DriverDTO driverDTO = new DriverDTO();
		driverDTO.setId(driver.getDriverId());
		driverDTO.setName(driver.getName());
		driverDTO.setLicenseNum(driver.getLicenseNum());
		driverDTO.setVehicleNum(driver.getVehicleNum());
		driverDTO.setContactNum(driver.getContactNum());
		driverDTO.setYearsOfExp(driver.getYearsOfExp());
		driverDTO.setState(driver.getState());

		if (driver.getAddress() != null) {
			AddressDTO addressDto = new AddressDTO();
			addressDto.setAddress_id(String.valueOf(driver.getAddress().getAddressId()));
			addressDto.setAddress_line1(driver.getAddress().getAddressLine1());
			addressDto.setAddress_line2(driver.getAddress().getAddressLine2());
			addressDto.setCity(driver.getAddress().getCity());
			driverDTO.setAddress(addressDto);
		}
		return driverDTO;
	}

	/**
	 * Converts a DriverDTO object to a Driver entity.
	 * 
	 * @param dto - the DriverDTO to convert
	 * @return Driver - the converted entity object
	 */
	public Driver toEntity(DriverDTO dto) {
		if (dto == null) {
			return null;
		}

		Driver driver = new Driver();
		driver.setDriverId(dto.getId());
		driver.setName(dto.getName());
		driver.setLicenseNum(dto.getLicenseNum());
		driver.setVehicleNum(dto.getVehicleNum());
		driver.setContactNum(dto.getContactNum());
		driver.setYearsOfExp(dto.getYearsOfExp());
		driver.setState(dto.getState());

		if (dto.getAddress() != null) {
			Address address = new Address();
			address.setAddressId(
					dto.getAddress().getAddress_id() == null ? null : Long.valueOf(dto.getAddress().getAddress_id()));
			address.setAddressLine1(dto.getAddress().getAddress_line1());
			address.setAddressLine2(dto.getAddress().getAddress_line2());
			address.setCity(dto.getAddress().getCity());
			driver.setAddress(address);
		}
		return driver;
	}

}
