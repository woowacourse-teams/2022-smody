import favicon from '../public/image/favicon.ico';
import theme from './theme';

import { addons } from '@storybook/addons';

addons.setConfig({
  theme,
});

/* set favicon */
const link = document.createElement('link');
link.setAttribute('rel', 'shortcut icon');
link.setAttribute('href', favicon);
document.head.appendChild(link);
