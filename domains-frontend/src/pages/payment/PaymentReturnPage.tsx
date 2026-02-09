import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router';
import Axios from 'axios';
import { getAccessToken } from '~/utils/authTokens';

interface PaymentStatusResponse {
  paymentId: string;
  status: string;
  paid: boolean;
  domainsCreated: boolean;
  operationStatus?: string;
}

const PaymentReturnPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const navigate ${DB_USER:***REMOVED***} useNavigate();
  const [searchParams] ${DB_USER:***REMOVED***} useSearchParams();
  const paymentId ${DB_USER:***REMOVED***} searchParams.get('paymentId');

  useEffect(() ${DB_USER:***REMOVED***}> {
    const checkPaymentStatus ${DB_USER:***REMOVED***} async () ${DB_USER:***REMOVED***}> {
      if (!paymentId) {
        navigate('/payment/fail');
        return;
      }

      try {
        const token ${DB_USER:***REMOVED***} getAccessToken();
        const { data } ${DB_USER:***REMOVED***} await Axios.post<PaymentStatusResponse>(
          `/api/payments/${paymentId}/check`,
          {},
          {
            headers: token ? { Authorization: `Bearer ${token}` } : {},
          }
        );

        if (data?.paid) {
          navigate(`/payment/success?paymentId${DB_USER:***REMOVED***}${paymentId}`);
        } else if (data?.status ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'FAILED') {
          navigate('/payment/fail');
        } else {
          navigate(`/payment/success?paymentId${DB_USER:***REMOVED***}${paymentId}`);
        }
      } catch (error) {
        console.error('Failed to check payment status:', error);
        navigate('/payment/fail');
      }
    };

    checkPaymentStatus();
  }, [paymentId, navigate]);

  return null;
};

export default PaymentReturnPage;
