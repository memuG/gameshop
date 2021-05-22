package com.przemo.gameshop.dto;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

import static com.przemo.gameshop.persistence.entities.GameEntityConstraints.*;

@Builder
@Data
public class GameEntityDto {

    @Size(min = MIN_TITLE_CHARS, max = MAX_TITLE_CHARS)
    @Column(unique = true)
    private String title;

    @Min(MIN_PRICE)
    @Max(MAX_PRICE)
    private BigDecimal price;
}
