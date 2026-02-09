import { Link } from '@chakra-ui/react';
import React, { type MouseEvent, type ReactNode } from 'react';
import { useNavigate, type NavigateOptions } from 'react-router';

type Props ${DB_USER:***REMOVED***} {
  children: ReactNode;
  to: string;
  options?: NavigateOptions;
};

const AppLink ${DB_USER:***REMOVED***} (props: Props) ${DB_USER:***REMOVED***}> {
  const navigate ${DB_USER:***REMOVED***} useNavigate();
  const handleClick ${DB_USER:***REMOVED***} (e: MouseEvent<HTMLAnchorElement>) ${DB_USER:***REMOVED***}> {
    e.preventDefault();

    navigate(props.to, props.options);
  };

  return <Link onClick${DB_USER:***REMOVED***}{handleClick}>{props.children}</Link>;
};

export default AppLink;
