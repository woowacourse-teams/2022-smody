import { auth } from 'mocks/handlers/auth';
import { challenge } from 'mocks/handlers/challenge';

export const handlers = [...challenge, ...auth];
