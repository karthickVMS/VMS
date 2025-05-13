package com.akt.vms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.akt.vms.dto.RouteMappingDTO;
import com.akt.vms.entity.RouteMapping;
import com.akt.vms.mapper.RouteMappingMapper;
import com.akt.vms.repository.RouteMappingRepository;

@ExtendWith(MockitoExtension.class)
public class RouteMappingServiceTest {

	@Mock
	private RouteMappingRepository routeMappingRepository;

	@Mock
	private RouteMappingMapper routeMappingMapper;

	@InjectMocks
	private RouteMappingService routeMappingService;

	private RouteMappingDTO routeMappingDTO;
	private RouteMapping routeMapping;

	@BeforeEach
	void setUp() {
		routeMappingDTO = new RouteMappingDTO();
		routeMappingDTO.setPoint_name("Point A");

		routeMapping = new RouteMapping();
		routeMapping.setPointName("Point A");
	}

	@Test
	void createRouteMapping_ShouldSaveAndReturnEntity() {
		// Arrange
		when(routeMappingMapper.toEntity(any(RouteMappingDTO.class))).thenReturn(routeMapping);
		when(routeMappingRepository.save(any(RouteMapping.class))).thenReturn(routeMapping);

		// Act
		RouteMapping result = routeMappingService.createRouteMapping(routeMappingDTO, 1L);

		// Assert
		assertNotNull(result);
		assertEquals("Point A", result.getPointName());
		verify(routeMappingRepository, times(1)).save(any(RouteMapping.class));
	}

	@Test
	void updateRouteMapping_ShouldUpdateExistingEntity() {
		// Arrange
		when(routeMappingRepository.findById(1L)).thenReturn(Optional.of(routeMapping));
		doNothing().when(routeMappingMapper).updateEntityFromDTO(any(RouteMappingDTO.class), eq(routeMapping));
		when(routeMappingRepository.save(routeMapping)).thenReturn(routeMapping);

		// Act
		RouteMapping updated = routeMappingService.updateRouteMapping(1L, routeMappingDTO);

		// Assert
		assertNotNull(updated);
		verify(routeMappingRepository).findById(1L);
		verify(routeMappingMapper).updateEntityFromDTO(routeMappingDTO, routeMapping);
		verify(routeMappingRepository).save(routeMapping);
	}

	@Test
	void updateRouteMapping_ShouldThrowException_WhenNotFound() {
		when(routeMappingRepository.findById(1L)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			routeMappingService.updateRouteMapping(1L, routeMappingDTO);
		});

		assertEquals("RouteMapping not found with ID: 1", exception.getMessage());
		verify(routeMappingRepository).findById(1L);
	}

	@Test
	void deleteRouteMapping_ShouldDelete_WhenExists() {
		when(routeMappingRepository.existsById(1L)).thenReturn(true);
		doNothing().when(routeMappingRepository).deleteById(1L);

		routeMappingService.deleteRouteMapping(1L);

		verify(routeMappingRepository).existsById(1L);
		verify(routeMappingRepository).deleteById(1L);
	}

	@Test
	void deleteRouteMapping_ShouldThrowException_WhenNotFound() {
		when(routeMappingRepository.existsById(1L)).thenReturn(false);

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			routeMappingService.deleteRouteMapping(1L);
		});

		assertEquals("RouteMapping not found with ID: 1", exception.getMessage());
		verify(routeMappingRepository).existsById(1L);
	}

}
