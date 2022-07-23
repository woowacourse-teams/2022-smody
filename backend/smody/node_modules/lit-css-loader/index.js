const loaderUtils = require("loader-utils");

module.exports = function loader(source) {
  const path = loaderUtils.getOptions(this).import || 'lit-element';
  return `
    import { css } from '${path}';
    export default css\`${ source.replace(/(`|\\|\${)/g, '\\$1') }\`;
  `;
}

