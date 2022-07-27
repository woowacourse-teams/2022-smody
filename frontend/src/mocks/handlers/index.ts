import { challenge } from 'mocks/handlers/challenge';
import { auth } from 'mocks/handlers/auth';

export const handlers = [...challenge, ...auth];
