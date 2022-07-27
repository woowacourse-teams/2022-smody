import { ValidatorFunction } from 'commonType';
import { useState, useEffect } from 'react';
import { ChangeEvent } from 'react';

const useInput = (
  initialValue: string | undefined,
  validator?: ValidatorFunction,
  optionalValue?: string,
) => {
  const [value, setValue] = useState(initialValue);
  const [isValidated, setIsValidated] = useState<boolean | undefined>();
  const [message, setMessage] = useState('');

  useEffect(() => {
    if (typeof validator === 'undefined' || value === undefined) {
      return;
    }

    const { isValidated: newIsValidated, message: newMessage } = validator(
      value,
      optionalValue,
    );
    setIsValidated(newIsValidated);
    setMessage(newMessage);
  }, [value, optionalValue]);

  useEffect(() => {
    setValue(initialValue);
  }, [initialValue]);

  const onChange = (event: ChangeEvent<HTMLInputElement>) => {
    const {
      target: { value },
    } = event;

    setValue(value);
  };

  return { value, onChange, isValidated, message };
};

export default useInput;
