package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotFoundExceptionTest {

    @Test
    void exception_ShouldBeRuntimeException() {
        NotFoundException exception = new NotFoundException();

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    void exception_ShouldHaveResponseStatusAnnotation() {
        ResponseStatus annotation = NotFoundException.class.getAnnotation(ResponseStatus.class);

        assertThat(annotation).isNotNull();
        assertThat(annotation.value()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void exception_CanBeThrown() {
        assertThatThrownBy(() -> {
            throw new NotFoundException();
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    void exception_ShouldBeCreatable() {
        NotFoundException exception = new NotFoundException();

        assertThat(exception).isNotNull();
    }

    @Test
    void exception_ShouldHaveNoMessage() {
        NotFoundException exception = new NotFoundException();

        assertThat(exception.getMessage()).isNull();
    }
}
