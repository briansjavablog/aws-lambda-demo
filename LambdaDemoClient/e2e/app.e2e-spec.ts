import { LambdaDemoCiientPage } from './app.po';

describe('lambda-demo-ciient App', () => {
  let page: LambdaDemoCiientPage;

  beforeEach(() => {
    page = new LambdaDemoCiientPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
