'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _schemaUtils = require('schema-utils');

var _schemaUtils2 = _interopRequireDefault(_schemaUtils);

var _lodash = require('lodash.uniq');

var _lodash2 = _interopRequireDefault(_lodash);

var _isGlob = require('is-glob');

var _isGlob2 = _interopRequireDefault(_isGlob);

var _glob = require('glob');

var _glob2 = _interopRequireDefault(_glob);

var _path = require('path');

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _toConsumableArray(arr) { if (Array.isArray(arr)) { for (var i = 0, arr2 = Array(arr.length); i < arr.length; i++) { arr2[i] = arr[i]; } return arr2; } else { return Array.from(arr); } }

function getFileAndContextDeps(compilation, files, dirs, cwd) {
  var fileDependencies = compilation.fileDependencies,
      contextDependencies = compilation.contextDependencies;

  var isWebpack4 = compilation.hooks;
  var fds = isWebpack4 ? [].concat(_toConsumableArray(fileDependencies)) : fileDependencies;
  var cds = isWebpack4 ? [].concat(_toConsumableArray(contextDependencies)) : contextDependencies;

  if (files.length > 0) {
    files.forEach(function (pattern) {
      var f = pattern;
      if ((0, _isGlob2.default)(pattern)) {
        f = _glob2.default.sync(pattern, {
          cwd: cwd,
          dot: true,
          absolute: true
        });
      }
      fds = fds.concat(f);
    });
    fds = (0, _lodash2.default)(fds);
  }
  if (dirs.length > 0) {
    cds = (0, _lodash2.default)(cds.concat(dirs));
  }

  return {
    fileDependencies: fds,
    contextDependencies: cds
  };
}

var ExtraWatchWebpackPlugin = function () {
  function ExtraWatchWebpackPlugin() {
    var options = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};

    _classCallCheck(this, ExtraWatchWebpackPlugin);

    (0, _schemaUtils2.default)(require('../options.json'), options, 'ExtraWatchWebpackPlugin'); // eslint-disable-line
    this.options = _extends({}, ExtraWatchWebpackPlugin.defaults, options);
  }

  _createClass(ExtraWatchWebpackPlugin, [{
    key: 'apply',
    value: function apply(compiler) {
      var _options = this.options,
          files = _options.files,
          dirs = _options.dirs;
      var cwd = this.options.cwd;

      files = typeof files === 'string' ? [files] : files;
      dirs = typeof dirs === 'string' ? [dirs] : dirs;
      if (compiler.hooks) {
        compiler.hooks.afterCompile.tap('after-compile', function (compilation) {
          var _getFileAndContextDep = getFileAndContextDeps(compilation, files, dirs, cwd),
              fileDependencies = _getFileAndContextDep.fileDependencies,
              contextDependencies = _getFileAndContextDep.contextDependencies;

          if (files.length > 0) {
            fileDependencies.forEach(function (file) {
              compilation.fileDependencies.add((0, _path.resolve)(file));
            });
          }
          if (dirs.length > 0) {
            contextDependencies.forEach(function (context) {
              compilation.contextDependencies.add(context);
            });
          }
        });
      } else {
        compiler.plugin('after-compile', function (compilation, callback) {
          var _getFileAndContextDep2 = getFileAndContextDeps(compilation, files, dirs, cwd),
              fileDependencies = _getFileAndContextDep2.fileDependencies,
              contextDependencies = _getFileAndContextDep2.contextDependencies;

          if (files.length > 0) {
            compilation.fileDependencies = fileDependencies; // eslint-disable-line
          }
          if (dirs.length > 0) {
            compilation.contextDependencies = contextDependencies; // eslint-disable-line
          }
          callback();
        });
      }
    }
  }]);

  return ExtraWatchWebpackPlugin;
}();

ExtraWatchWebpackPlugin.defaults = {
  cwd: process.cwd(),
  files: [],
  dirs: []
};
exports.default = ExtraWatchWebpackPlugin;
module.exports = exports['default'];