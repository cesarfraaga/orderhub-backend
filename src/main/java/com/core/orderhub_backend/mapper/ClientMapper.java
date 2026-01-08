package com.core.orderhub_backend.mapper;


import com.core.orderhub_backend.dto.ClientDto;
import com.core.orderhub_backend.entity.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientDto toDto(Client client);

    Client toEntity(ClientDto clientDTO);
}
