/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/date/filters';
import PurchaseOrderService from '@/entities/purchase-order/purchase-order.service';
import { PurchaseOrder } from '@/shared/model/purchase-order.model';
import { DiscountType } from '@/shared/model/enumerations/discount-type.model';
import { PaymentMethod } from '@/shared/model/enumerations/payment-method.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('PurchaseOrder Service', () => {
    let service: PurchaseOrderService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new PurchaseOrderService();
      currentDate = new Date();
      elemDefault = new PurchaseOrder(
        123,
        currentDate,
        currentDate,
        0,
        0,
        0,
        DiscountType.PERCENTAGE,
        0,
        false,
        PaymentMethod.Cash,
        'AAAAAAA',
        false,
        PaymentMethod.Cash,
        'AAAAAAA',
        0,
        0,
        0
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            invoiceDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
            issueDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a PurchaseOrder', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
            invoiceDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
            issueDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            invoiceDate: currentDate,
            issueDate: currentDate,
          },
          returnedFromService
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a PurchaseOrder', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a PurchaseOrder', async () => {
        const returnedFromService = Object.assign(
          {
            invoiceDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
            issueDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
            paymentTerms: 1,
            discount: 1,
            notes: 1,
            discountType: 'BBBBBB',
            depositAmount: 1,
            isDepositPaied: true,
            depositMethod: 'BBBBBB',
            depositPayRef: 'BBBBBB',
            isAlreadyPaied: true,
            paymentMethod: 'BBBBBB',
            paymentRef: 'BBBBBB',
            amount: 1,
            lastAmount: 1,
            shippingFees: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            invoiceDate: currentDate,
            issueDate: currentDate,
          },
          returnedFromService
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a PurchaseOrder', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a PurchaseOrder', async () => {
        const patchObject = Object.assign(
          {
            paymentTerms: 1,
            discount: 1,
            notes: 1,
            discountType: 'BBBBBB',
            depositAmount: 1,
            depositPayRef: 'BBBBBB',
            isAlreadyPaied: true,
            amount: 1,
          },
          new PurchaseOrder()
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            invoiceDate: currentDate,
            issueDate: currentDate,
          },
          returnedFromService
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a PurchaseOrder', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of PurchaseOrder', async () => {
        const returnedFromService = Object.assign(
          {
            invoiceDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
            issueDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
            paymentTerms: 1,
            discount: 1,
            notes: 1,
            discountType: 'BBBBBB',
            depositAmount: 1,
            isDepositPaied: true,
            depositMethod: 'BBBBBB',
            depositPayRef: 'BBBBBB',
            isAlreadyPaied: true,
            paymentMethod: 'BBBBBB',
            paymentRef: 'BBBBBB',
            amount: 1,
            lastAmount: 1,
            shippingFees: 1,
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            invoiceDate: currentDate,
            issueDate: currentDate,
          },
          returnedFromService
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of PurchaseOrder', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a PurchaseOrder', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a PurchaseOrder', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
