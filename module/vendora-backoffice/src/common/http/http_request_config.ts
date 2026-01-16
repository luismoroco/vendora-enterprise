export type Params = {[key: string]: any};
export type QueryParams = Params & {};
export type Body = Params & {};
export type Header = Params & {};
export type PathVariables = Params & {};
export type HttpRequestConfig = {
  route?: string,
  queryParams?: QueryParams,
  body?: Body,
  headers?: Header,
  pathVariables?: PathVariables,
};
