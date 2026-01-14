package com.core.orderhub.backend.mapper;


import com.core.orderhub.backend.domain.entity.Client;
import com.core.orderhub.backend.dto.ClientDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientDto toDto(Client client);

    Client toEntity(ClientDto clientDTO);
}
