# Changelog

## 1.0.0

### Added
- kotlin compiler - allWarningsAsErrors
  - 'toLowerCase(): String' is deprecated. Use lowercase() instead
  - allWarningsAsErrors was enabled
- [Kover](https://github.com/Kotlin/kotlinx-kover)
- GitHub Packages
  - Publishing to GitHub Packages
  - GPG signing

### Changed
- Use messages from properties file while logging
- Common logic was moved to `GenerationStrategy`
  - `GenerationStrategy` now is an abstract class instead of interface with abstract property objectMapper
  - `SchemaFileSettings.format` was unmarked as deprecated
- Build jar with JavaDoc and Sources
- dependencies:
  - org.junit.jupiter:junit-jupiter-api: 5.6.0 -> 5.8.2
  - org.junit.jupiter:junit-jupiter-params: 5.6.0 -> 5.8.2
  - org.reflections:reflections: 0.9.12 -> 0.10.2