package com.sweak.spotifylibraryfeatures.features.login

import androidx.test.filters.SmallTest
import org.junit.Assert.*
import org.junit.Test

@SmallTest
class AuthorizationFlowTest {

    @Test
    fun shouldNotContainUrlUnsafeCharacters_forNewCodeVerifier() {
        val codeVerifier = AuthorizationFlow.CODE_VERIFIER

        assertTrue(codeVerifier.count { (it == '+' || it == '\\') } == 0)
    }

    @Test
    fun shouldNotContainLineTerminatingCharacters_forNewCodeVerifier() {
        val codeVerifier = AuthorizationFlow.CODE_VERIFIER

        assertTrue(codeVerifier.count {
            (it == '\n' || it == '\r' || it == '\u0085' || it == '\u2028' || it == '\u2029')
        } == 0)
    }

    @Test
    fun shouldNotContainPaddingCharacters_forNewCodeVerifier() {
        val codeVerifier = AuthorizationFlow.CODE_VERIFIER

        assertFalse(codeVerifier.endsWith('='))
    }

    @Test
    fun shouldBeBetween43And128Characters_forNewCodeVerifier() {
        val codeVerifier = AuthorizationFlow.CODE_VERIFIER

        assertTrue(codeVerifier.length in 43..128)
    }

    @Test
    fun shouldNotContainUrlUnsafeCharacters_forNewCodeChallenge() {
        val codeChallenge = AuthorizationFlow.getCodeChallenge(AuthorizationFlow.CODE_VERIFIER)

        assertTrue(codeChallenge.count { (it == '+' || it == '\\') } == 0)
    }

    @Test
    fun shouldNotContainLineTerminatingCharacters_forNewCodeChallenge() {
        val codeChallenge = AuthorizationFlow.getCodeChallenge(AuthorizationFlow.CODE_VERIFIER)

        assertTrue(codeChallenge.count {
            (it == '\n' || it == '\r' || it == '\u0085' || it == '\u2028' || it == '\u2029')
        } == 0)
    }

    @Test
    fun shouldNotContainPaddingCharacters_forNewCodeChallenge() {
        val codeChallenge = AuthorizationFlow.getCodeChallenge(AuthorizationFlow.CODE_VERIFIER)

        assertFalse(codeChallenge.endsWith('='))
    }
}