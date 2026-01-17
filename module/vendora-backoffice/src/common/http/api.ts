export interface ApiResponse<T> {
  status: number;
  data: T
}

export interface ApiException {
  status: number,
  type: string,
  message: string,
  time: Date,
}