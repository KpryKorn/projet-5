package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BadRequestExceptionTest {

    @Test
    void exception_ShouldBeRuntimeException() {
        BadRequestException exception = new BadRequestException();

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    void exception_ShouldHaveResponseStatusAnnotation() {
        ResponseStatus annotation = BadRequestException.class.getAnnotation(ResponseStatus.class);

        assertThat(annotation).isNotNull();
        assertThat(annotation.value()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void exception_CanBeThrown() {
        assertThatThrownBy(() -> {
            throw new BadRequestException();
        }).isInstanceOf(BadRequestException.class);
    }

    @Test
    void exception_ShouldBeCreatable() {
        BadRequestException exception = new BadRequestException();

        assertThat(exception).isNotNull();
    }

    @Test
    void exception_ShouldHaveNoMessage() {
        BadRequestException exception = new BadRequestException();

        assertThat(exception.getMessage()).isNull();
    }
}
