import { Text } from '@chakra-ui/react';
import type { Dayjs } from 'dayjs';
import dayjs from 'dayjs';
import React, { type JSX } from 'react';

type Props ${DB_USER:***REMOVED***} {
  children: string | Date | Dayjs;
  as?: string | (() ${DB_USER:***REMOVED***}> JSX.Element);
};

const DateText ${DB_USER:***REMOVED***} (props: Props) ${DB_USER:***REMOVED***}> {
  const date ${DB_USER:***REMOVED***} dayjs(props.children);
  const format ${DB_USER:***REMOVED***} dayjs().year() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} date.year() ? 'DD MMMM' : 'DD MMM YYYY';
  const dateText ${DB_USER:***REMOVED***} date.locale('ru').format(format);

  if (props.as) {
    return React.createElement(props.as, null, dateText);
  }

  return <Text>{dateText}</Text>;
};

export default DateText;
