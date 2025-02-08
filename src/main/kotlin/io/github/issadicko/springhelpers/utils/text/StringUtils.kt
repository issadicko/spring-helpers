package io.github.issadicko.springhelpers.utils.text

import java.text.Normalizer
import java.util.*
import kotlin.text.Regex

/**
 * Utility class providing enhanced string manipulation functionalities.
 * This includes text normalization, sanitization, formatting, and various string operations.
 */
object StringUtils {
    private const val DEFAULT_TRUNCATE_SUFFIX = "..."
    private const val DEFAULT_ELLIPSIS = "..."
    private const val SLUG_SEPARATOR = "-"
    
    private val SPECIAL_CHARS_REGEX = Regex("[^a-zA-Z0-9-,\\s]")
    private val WHITESPACE_REGEX = Regex("\\s+")
    private val MULTIPLE_DASHES_REGEX = Regex("-+")
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
    private val URL_REGEX = Regex(
        """^(?=[A-Z0-9][A-Z0-9@._%+-]{5,253}$)[A-Z0-9._%+-]{1,64}@(?:(?=[A-Z0-9-]{1,63}\.)[A-Z0-9]+(?:-[A-Z0-9]+)*\.){1,8}[A-Z]{2,63}$""",
        RegexOption.IGNORE_CASE
    )
    
    private val CHAR_MAPPINGS = mapOf(
        "àâäáãå" to "a",
        "éèêëẽ" to "e",
        "îïíĩ" to "i",
        "ôöóõ" to "o",
        "ùûüúũ" to "u",
        "ÿý" to "y",
        "ç" to "c",
        "ñ" to "n",
        "ß" to "ss"
    )

    /**
     * Removes diacritical marks and converts to lowercase.
     * @param text The input text to process
     * @return Normalized text without accents
     */
    fun removeAccents(text: String): String = text
        .let { Normalizer.normalize(it, Normalizer.Form.NFD) }
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        .lowercase()

    /**
     * Converts text to URL-friendly slug.
     * @param text The input text to convert
     * @param lowercase Whether to convert to lowercase (default: true)
     * @return URL-friendly slug
     */
    fun slugify(text: String, lowercase: Boolean = true): String = text
        .let { if (lowercase) it.lowercase(Locale.getDefault()) else it }
        .let { removeAccents(it) }
        .replace(SPECIAL_CHARS_REGEX, "")
        .replace(WHITESPACE_REGEX, SLUG_SEPARATOR)
        .replace(MULTIPLE_DASHES_REGEX, SLUG_SEPARATOR)
        .trim('-')

    /**
     * Truncates text to specified length with custom suffix.
     * @param text The input text to truncate
     * @param maxLength Maximum length of the result
     * @param suffix Suffix to append (default: "...")
     * @return Truncated text
     */
    fun truncate(text: String, maxLength: Int, suffix: String = DEFAULT_TRUNCATE_SUFFIX): String {
        if (text.length <= maxLength) return text
        val adjustedLength = maxLength - suffix.length
        return if (adjustedLength <= 0) suffix
        else text.take(adjustedLength) + suffix
    }

    /**
     * Capitalizes first letter of each word in the text.
     * @param text The input text to capitalize
     * @return Text with first letter of each word capitalized
     */
    fun capitalizeWords(text: String): String =
        text.split(" ")
            .map { it.capitalize() }
            .joinToString(" ")

    /**
     * Extracts first letter of each word to create initials.
     * @param text The input text to process
     * @param uppercase Whether to convert to uppercase (default: true)
     * @return String containing initials
     */
    fun getInitials(text: String, uppercase: Boolean = true): String =
        text.trim().split(" ")
            .filter { it.isNotBlank() }
            .map { it.first() }
            .joinToString("")
            .let { if (uppercase) it.uppercase() else it }

    /**
     * Masks part of the text, useful for sensitive data.
     * @param text Text to mask
     * @param start Start index to begin masking
     * @param maskChar Character to use for masking (default: '*')
     * @return Masked text
     */
    fun mask(text: String, start: Int, maskChar: Char = '*'): String {
        if (start >= text.length) return text
        return text.take(start) + maskChar.toString().repeat(text.length - start)
    }

    /**
     * Checks if string is a valid email address.
     * @param email Email address to validate
     * @return true if email is valid
     */
    fun isValidEmail(email: String): Boolean = EMAIL_REGEX.matches(email)

    /**
     * Checks if string is a valid URL.
     * @param url URL to validate
     * @return true if URL is valid
     */
    fun isValidUrl(url: String): Boolean = URL_REGEX.matches(url)

    /**
     * Removes all whitespace from the string.
     * @param text Input text
     * @return Text with all whitespace removed
     */
    fun removeWhitespace(text: String): String = text.replace("\\s+".toRegex(), "")

    /**
     * Replaces accented characters with their non-accented equivalents.
     * @param text Input text containing accented characters
     * @return Text with accented characters replaced
     */
    fun replaceAccentedChars(text: String): String {
        var result = text
        CHAR_MAPPINGS.forEach { (accented, replacement) ->
            accented.forEach { char ->
                result = result.replace(char.toString(), replacement)
            }
        }
        return result
    }

    /**
     * Centers text within specified width.
     * @param text Text to center
     * @param width Total width
     * @param padChar Character to use for padding (default: space)
     * @return Centered text
     */
    fun center(text: String, width: Int, padChar: Char = ' '): String {
        if (text.length >= width) return text
        val leftPad = (width - text.length) / 2
        val rightPad = width - text.length - leftPad
        return padChar.toString().repeat(leftPad) + text + padChar.toString().repeat(rightPad)
    }

    /**
     * Reverses a string while preserving word positions.
     * @param text Input text to reverse
     * @return Text with reversed characters but preserved word positions
     */
    fun reversePreserveWords(text: String): String =
        text.split(" ")
            .map { it.reversed() }
            .joinToString(" ")

    private fun String.capitalize(): String =
        if (isNotEmpty()) this[0].uppercase() + substring(1) else this
}