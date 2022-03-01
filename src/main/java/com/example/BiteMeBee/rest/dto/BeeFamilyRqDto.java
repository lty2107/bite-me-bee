package com.example.BiteMeBee.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Входной DTO пчелиной семьи")
public class BeeFamilyRqDto {

  @NotNull(message = "Не указан идентификатор пчелиной семьи")
  @Schema(description = "Идентификатор пчелиной семьи", example = "1")
  private Long beeTypeId;

  @Size(max = 1000, message = "Длинна заметки должна быть не больше 1000 символов")
  @NotNull(message = "Не указана заметка о пчелиной семье")
  @Schema(description = "Заметка о пчелиной семье",
    example = "Семья, которая постоянно болеет, но достаточно продуктивна на мёд", maxLength = 1000)
  private String note;

  @NotNull(message = "Не указан статус жизни пчёл")
  @Schema(description = "Живая ли пчелиная семья", example = "true")
  private Boolean status;
}
