# Change Log

All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).
This change log adheres to standards from [Keep a CHANGELOG](http://keepachangelog.com).

## [0.0.4] - 2020-08-28
### Fixed
- Fix build failure by escaping special characters

Fixes #1

Escapes backtick, backslash and ${ with a backslash when injecting the source CSS into a template literal in the generated ES module.


