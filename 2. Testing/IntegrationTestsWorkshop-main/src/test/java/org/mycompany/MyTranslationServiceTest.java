package org.mycompany;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyTranslationServiceTest {

    @Mock
    private Translate googleTranslate;

    /**
     * 1. Happy case test.
     * <p>
     * When `MyTranslationService::translateWithGoogle` method is called with any sentence and target language is equal to "ru",
     * `googleTranslate` dependency should be called and `translation.getTranslatedText()` returned.
     * No other interactions with `googleTranslate` dependency should be invoked apart from a single call to `googleTranslate.translate()`.
     */
    @Test
    void translateWithGoogle_anySentenceAndTargetLanguageIsRu_success() {
        // Arrange
        MyTranslationService service = new MyTranslationService(googleTranslate);
        String input = "Hello world";
        String expectedTranslation = "Привет мир";

        Translation translationMock = mock(Translation.class);
        when(translationMock.getTranslatedText()).thenReturn(expectedTranslation);
        when(googleTranslate.translate(eq(input), any())).thenReturn(translationMock);

        // Act
        String result = service.translateWithGoogle(input, "ru");

        // Assert
        assertEquals(expectedTranslation, result);
        verify(googleTranslate, times(1)).translate(eq(input), any());
        verifyNoMoreInteractions(googleTranslate);

    }

    /**
     * 2. Unhappy case test when target language is not supported.
     * <p>
     * When `MyTranslationService::translateWithGoogle` method is called with any sentence and target language is not equal to "ru",
     * `IllegalArgumentException` should be thrown. `googleTranslate` dependency should not be called at all.
     */
    @Test
    void translateWithGoogle_anySentenceAndTargetLanguageIsNotRu_failure() {
        // Arrange
        MyTranslationService service = new MyTranslationService(googleTranslate);
        String input = "Hello world";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.translateWithGoogle(input, "es"));

        assertEquals("only translation to Russian is currently supported!", exception.getMessage());
        verifyNoInteractions(googleTranslate);
    }

    /**
     * 3. Unhappy case test when Google Translate call throws exception.
     * <p>
     * When `MyTranslationService::translateWithGoogle` method is called with any sentence and target language is equal to "ru",
     * `googleTranslate` dependency should be called. When `googleTranslate` dependency throws exception, it should be
     * wrapped into `MyTranslationServiceException` and the latter should be thrown from our method.
     */
    @Test
    void translateWithGoogle_googleTranslateThrowsException_failure() {
        // Arrange
        MyTranslationService service = new MyTranslationService(googleTranslate);
        String input = "Hello world";
        RuntimeException originalException = new RuntimeException("API error");

        when(googleTranslate.translate(eq(input), any())).thenThrow(originalException);

        // Act & Assert
        MyTranslationServiceException exception = assertThrows(MyTranslationServiceException.class,
                () -> service.translateWithGoogle(input, "ru"));

        assertEquals("Exception while calling Google Translate API", exception.getMessage());
        assertSame(originalException, exception.getCause());
        verify(googleTranslate, times(1)).translate(eq(input), any());
    }
}