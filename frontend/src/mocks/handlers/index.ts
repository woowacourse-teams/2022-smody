import { auth } from 'mocks/handlers/auth';
import { challenge } from 'mocks/handlers/challenge';
import { feed } from 'mocks/handlers/feed';
import { push } from 'mocks/handlers/push';
import { ranking } from 'mocks/handlers/ranking';

export const handlers = [...challenge, ...auth, ...push, ...feed, ...ranking];
