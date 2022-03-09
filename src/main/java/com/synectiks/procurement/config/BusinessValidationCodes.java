package com.synectiks.procurement.config;

import org.springframework.lang.Nullable;
public enum BusinessValidationCodes {

	NEGATIVE_ID_NOT_ALLOWED(800, "Negative id not allowed"),
	ID_NOT_FOUND(801, "Id not found"),
	DELETION_FAILED(802, "Deletion failed"),
	DATA_NOT_FOUND(803, "Data not found"),
	JSON_MAPPING_EXCEPTION(804, "JSON mapping exception"),
	JSON_PROCESSING_EXCEPTION(805, "JSON processing exception"),
	JSON_EXCEPTION(806, "JSON exception"),
	IO_EXCEPTION(807, "IO exception"),
	PARSE_EXCEPTION(808, "Parse exception"),
	
	NO_APPROVAL_RIGHTS(820, "Current role cannot approve");

	private final int value;

	private final String reasonPhrase;


	BusinessValidationCodes(int value, String reasonPhrase) {
		this.value = value;
		this.reasonPhrase = reasonPhrase;
	}


	/**
	 * Return the integer value of this status code.
	 */
	public int value() {
		return this.value;
	}

	/**
	 * Return the reason phrase of this status code.
	 */
	public String getReasonPhrase() {
		return this.reasonPhrase;
	}

	/**
	 * Return the status series of this status code.
	 * @see BusinessValidationCodes.Series
	 */
	public Series series() {
		return Series.valueOf(this);
	}

	/**
	 * Return a string representation of this status code.
	 */
	@Override
	public String toString() {
		return this.value + " " + name();
	}


	/**
	 * Return the enum constant of this type with the specified numeric value.
	 * @param statusCode the numeric value of the enum to be returned
	 * @return the enum constant with the specified numeric value
	 * @throws IllegalArgumentException if this enum has no constant for the specified numeric value
	 */
	public static BusinessValidationCodes valueOf(int statusCode) {
		BusinessValidationCodes status = resolve(statusCode);
		if (status == null) {
			throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
		}
		return status;
	}

	/**
	 * Resolve the given status code to an {@code HttpStatus}, if possible.
	 * @param statusCode the BusinessValidationCodes (potentially non-standard)
	 */
	@Nullable
	public static BusinessValidationCodes resolve(int statusCode) {
		for (BusinessValidationCodes status : values()) {
			if (status.value == statusCode) {
				return status;
			}
		}
		return null;
	}


	/**
	 * Enumeration of BusinessValidationCodes series.
	 */
	public enum Series {

		INFORMATIONAL(1),
		SUCCESSFUL(2),
		REDIRECTION(3),
		CLIENT_ERROR(4),
		SERVER_ERROR(5),
		VALIDATION_ERROR(6);
		
		private final int value;

		Series(int value) {
			this.value = value;
		}

		/**
		 * Return the integer value of this status series. Ranges from 1 to 6.
		 */
		public int value() {
			return this.value;
		}

		/**
		 * Return the enum constant of this type with the corresponding series.
		 * @param status a standard BusinessValidationCodes
		 * @return the enum constant of this type with the corresponding series
		 * @throws IllegalArgumentException if this enum has no corresponding constant
		 */
		public static Series valueOf(BusinessValidationCodes status) {
			return valueOf(status.value);
		}

		/**
		 * Return the enum constant of this type with the corresponding series.
		 * @return the enum constant of this type with the corresponding series
		 * @throws IllegalArgumentException if this enum has no corresponding constant
		 */
		public static Series valueOf(int statusCode) {
			Series series = resolve(statusCode);
			if (series == null) {
				throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
			}
			return series;
		}

		/**
		 * Resolve the given status code to an {@code BusinessValidationCodes.Series}, if possible.
		 * @param BusinessValidationCodes (potentially non-standard)
		 */
		@Nullable
		public static Series resolve(int statusCode) {
			int seriesCode = statusCode / 100;
			for (Series series : values()) {
				if (series.value == seriesCode) {
					return series;
				}
			}
			return null;
		}
	}

}

