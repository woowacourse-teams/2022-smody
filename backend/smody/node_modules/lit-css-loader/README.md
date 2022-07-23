# Usage

```
npm i -D lit-css-loader
```

```js
module: {
  rules: [
    {
      test: /\.css$/,
      loader: 'lit-css-loader',
      options: {
        import: 'lit' // defaults to lit-element
      }
    }
  ]
}
```

```js
import { LitElement, html, customElement } from 'lit-element'

import style from './styled-el.css'

@customElement('styled-el')
export class extends LitElement {
  static styles = [style]
  render() {
    return html`<p>such style. very win</p>`
  }
}
```

Looking for rollup? [rollup-plugin-lit-css](https://npm.im/rollup-plugin-lit-css)
