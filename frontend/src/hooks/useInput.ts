import { ValidatorFunction } from 'commonType';
import { useState, useEffect } from 'react';

const useInput = (
  initialValue: string,
  validator?: ValidatorFunction,
  optionalValue?: string,
) => {
  const [value, setValue] = useState(initialValue);
  const [isValidated, setIsValidated] = useState(false);
  const [message, setMessage] = useState('');

  useEffect(() => {
    if (typeof validator === 'undefined') {
      return;
    }

    const { isValidated: newIsValidated, message: newMessage } = validator(
      value,
      optionalValue,
    );
    setIsValidated(newIsValidated);
    setMessage(newMessage);
  }, [value, optionalValue]);

  const onChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const {
      target: { value },
    } = event;

    setValue(value);
  };

  return { value, onChange, isValidated, message };
};

export default useInput;
