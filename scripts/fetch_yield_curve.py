#!/usr/bin/env python
"""
Fetch historical yield curve data from ChinaBond (中债信息网)
https://www.chinabond.com.cn

This uses the same source as AKShare's bond_china_yield()

Usage:
    python fetch_yield_curve_chinabond.py --start 20260328 --end 20260328
"""

import argparse
import json
import sys
from io import StringIO

import pandas as pd
import requests


def fetch_yield_curve(start_date: str, end_date: str) -> pd.DataFrame:
    """
    Fetch historical yield curve data from ChinaBond

    Args:
        start_date: Start date in YYYYMMDD format
        end_date: End date in YYYYMMDD format (max 1 year range)

    Returns:
        DataFrame with columns: 曲线名称, 日期, 3月, 6月, 1年, 3年, 5年, 7年, 10年, 30年
    """
    url = "https://yield.chinabond.com.cn/cbweb-pbc-web/pbc/historyQuery"
    params = {
        "startDate": f"{start_date[:4]}-{start_date[4:6]}-{start_date[6:]}",
        "endDate": f"{end_date[:4]}-{end_date[4:6]}-{end_date[6:]}",
        "gjqx": "0",
        "qxId": "ycqx",
        "locale": "cn_ZH",
    }
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
        "(KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36",
    }
    res = requests.get(url, params=params, headers=headers)
    data_text = res.text.replace("&nbsp", "")

    try:
        data_df = pd.read_html(StringIO(data_text), header=0)[1]
    except Exception as e:
        print(f"Error parsing HTML: {e}", file=sys.stderr)
        return pd.DataFrame()

    if "日期" not in data_df.columns:
        print("No data found for the specified date range", file=sys.stderr)
        return pd.DataFrame()

    data_df["日期"] = pd.to_datetime(data_df["日期"], errors="coerce").dt.date

    tenors = ["3月", "6月", "1年", "3年", "5年", "7年", "10年", "30年"]
    for tenor in tenors:
        if tenor in data_df.columns:
            data_df[tenor] = pd.to_numeric(data_df[tenor], errors="coerce")

    data_df.sort_values(by="日期", inplace=True)
    data_df.reset_index(inplace=True, drop=True)

    return data_df


def main():
    parser = argparse.ArgumentParser(description="Fetch historical yield curve from ChinaBond")
    parser.add_argument("--start", required=True, help="Start date (YYYYMMDD)")
    parser.add_argument("--end", required=True, help="End date (YYYYMMDD)")

    args = parser.parse_args()

    print(f"Fetching yield curve from {args.start} to {args.end}...", file=sys.stderr)

    df = fetch_yield_curve(args.start, args.end)

    if df.empty:
        print(json.dumps({"error": "No data found"}))
        sys.exit(1)

    result = df.to_dict(orient="records")
    print(json.dumps(result, ensure_ascii=False, default=str))


if __name__ == "__main__":
    main()
