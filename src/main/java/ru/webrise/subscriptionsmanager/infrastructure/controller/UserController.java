package ru.webrise.subscriptionsmanager.infrastructure.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.webrise.subscriptionsmanager.infrastructure.validator.UuidNotBlank;
import ru.webrise.subscriptionsmanager.logic.UserService;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/${spring.application.name}/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable("id") @UuidNotBlank String userId) {
        final UserDto userDto = userService.getUserInfo(UUID.fromString(userId));

        return Objects.nonNull(userDto) ? ResponseEntity.ok(userDto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserDto request) {
        return ResponseEntity.ok(userService.saveUser(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@UuidNotBlank @PathVariable("id") String userId,
                                        @RequestBody UserDto request) {
        userService.updateUser(UUID.fromString(userId), request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@UuidNotBlank @PathVariable("id") String userId) {
        userService.deleteUser(UUID.fromString(userId));

        return ResponseEntity.ok().build();
    }

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record UserDto(
            @JsonProperty("user_id")
            String user_id,
            @NotBlank(message = "Имя пользователя не передано или пусто")
            @JsonProperty("first_name")
            String firstName,
            @NotBlank(message = "Фамилия пользователя не передана или пуста")
            @JsonProperty("last_name")
            String lastName,
            @NotBlank(message = "Номер телефона пользователя не передан или пуст")
            @JsonProperty("phone_number")
            String phoneNumber
    ) {
    }
}
