import { auth } from 'mocks/handlers/auth';
import { challenge } from 'mocks/handlers/challenge';
import { push } from 'mocks/handlers/push';

export const handlers = [...challenge, ...auth];
