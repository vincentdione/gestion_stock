package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.ModePayement;
import com.ovd.gestionstock.models.Unite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModePayementDto {

    private Long id;
    private String code;
    private String designation;

    public static ModePayementDto fromEntity (ModePayement mode){
        if (mode == null){
            return null;
        }

        return ModePayementDto.builder()
                .id(mode.getId())
                .code(mode.getCode())
                .designation(mode.getDesignation())
                 .build();

    }

    public static ModePayement toEntity (ModePayementDto dto){
        if (dto == null){
            return null;
        }

        return ModePayement.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .designation(dto.getDesignation())
                .build();
    }

}
